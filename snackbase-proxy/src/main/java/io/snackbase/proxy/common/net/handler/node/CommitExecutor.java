package io.snackbase.proxy.common.net.handler.node;

import io.snackbase.proxy.common.net.exception.FunctionNotSupportException;
import io.snackbase.proxy.common.net.handler.backend.BackendConnection;
import io.snackbase.proxy.common.net.handler.session.FrontendSession;
import io.snackbase.proxy.common.net.proto.mysql.BinaryPacket;
import io.snackbase.proxy.common.net.proto.mysql.ErrorPacket;
import io.snackbase.proxy.common.net.proto.mysql.OkPacket;
import io.snackbase.proxy.common.net.proto.util.ErrorCode;
import io.snackbase.proxy.common.net.route.RouteResultset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * CommitExecutor
 *
 * @Author lizhuyang
 */
public class CommitExecutor extends MultiNodeHandler implements ResponseHandler {

    private static final Logger logger = LoggerFactory.getLogger(CommitExecutor.class);

    private FrontendSession session;
    // 影响的行数,Okay包用
    private volatile long affectedRows;

    public CommitExecutor(FrontendSession session) {
        this.session = session;
    }

    public void commit() {
        if (session.isTxInterrupted()) {
            writeErrMessage(ErrorCode.ER_YES,
                    "Transaction errorMessage, need to rollback.");
            return;
        }
        final int initCount = session.getTarget().size();
        // 重置Multi状态
        reset(initCount);
        if (initCount <= 0) {
            writeOk();
            return;
        }
        if (initCount == 1) {
            BackendConnection backend = session.getTarget().elements().nextElement();
            backend.postCommand(session.getSource().getCommitCommand());
            backend.fireCmd();
            return;
        }
        // 多节点commit的情况,慎用
        for (BackendConnection backend : session.getTarget().values()) {
            backend.postCommand(session.getSource().getCommitCommand());
            backend.fireCmd();
        }
        return;
    }

    public void errorResponse(BinaryPacket bin) {
        if (session.getTargetCount() == 1) {
            handleSingleErrorResponse(bin);
            return;
        } else {
            handleMutliErrorResponse(bin);
        }
    }

    private void handleSingleErrorResponse(BinaryPacket bin) {
        ErrorPacket error = new ErrorPacket();
        error.read(bin);
        session.setTxInterrupt(new String(error.message));
        bin.write(session.getCtx());
    }

    private void handleMutliErrorResponse(BinaryPacket bin) {
        ErrorPacket err = new ErrorPacket();
        err.read(bin);
        String errorMessage = new String(err.message);
        logger.error("errorMessage packet " + errorMessage);
        lock.lock();
        try {
            // 但凡有一个error,就发送error信息
            if (isFailed.compareAndSet(false, true)) {
                this.setFailed(errorMessage,err.errno);
                // 设置事务被打断
                session.setTxInterrupt(errorMessage);
            }
            if(decrementCountBy()){
               notifyFailure();
            }
            // 能到达这里,肯定非自动提交,所以需要等待rollback,不需要释放
        } finally {
            lock.unlock();
        }
    }

    public void okResponse(BinaryPacket bin) {
        if (session.getTargetCount() == 1) {
            handleSingleOkResponse(bin);
            return;
        } else {
            handleMutliOkResponse(bin);
            return;
        }
    }

    private void handleSingleOkResponse(BinaryPacket bin) {
        bin.write(session.getCtx());
        // 提交成功 release
        session.release();
    }

    private void handleMutliOkResponse(BinaryPacket bin) {
        OkPacket ok = new OkPacket();
        ok.read(bin);
        lock.lock();
        try {
            affectedRows += ok.affectedRows;
            if (decrementCountBy()) {
                // OK Response 只有在最后一个Okay到达且前面都不出错的时候才传送
                if (!isFailed.get()) {
                    ok.affectedRows = affectedRows;
                    // lastInsertId
                    logger.info("multi last insert id =" + ok.insertId);
                    session.getSource().setLastInsertId(ok.insertId);
                    ok.write(session.getCtx());
                    // 提交成功 release
                    session.release();
                }else{
                    notifyFailure();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void writeErrMessage(int errno, String msg) {
        logger.warn(String.format("[FrontendConnection]ErrorNo=%d,ErrorMsg=%s", errno, msg));
        session.getSource().writeErrMessage((byte) 1, errno, msg);
    }

    public void writeOk() {
        session.getSource().writeOk();
    }

    public void rowResponse(BinaryPacket bin) {
        throw new FunctionNotSupportException("rowResponse not support");
    }

    public void lastEofResponse(BinaryPacket bin) {
        throw new FunctionNotSupportException("lastEofResponse not support");
    }

    public void execute(RouteResultset rrs) {
        throw new FunctionNotSupportException("execute not support");
    }

    public void fieldListResponse(List<BinaryPacket> fieldList) {
        throw new FunctionNotSupportException("fieldListResponse not support");
    }

    private void notifyFailure(){
        ErrorPacket error = new ErrorPacket();
        error.message = errorMessage.getBytes();
        error.errno = errno;
        error.packetId = ++packetId;// ERROR_PACKET
        error.write(session.getCtx());
        // 所有情况都release
        session.release();
    }

}
