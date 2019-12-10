/*
 * Copyright (C) 2016 alchemystar, Inc. All Rights Reserved.
 */
package io.snackbase.proxy.common.net.handler.frontend;

import alchemystar.lancelot.common.net.proto.MySQLPacket;
import alchemystar.lancelot.common.net.proto.mysql.BinaryPacket;
import alchemystar.lancelot.common.net.proto.util.ErrorCode;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 命令Handler
 *
 * @Author lizhuyang
 */
public class FrontendCommandHandler extends ChannelHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ChannelHandlerAdapter.class);

    protected FrontendConnection source;

    public FrontendCommandHandler(FrontendConnection source) {
        this.source = source;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        BinaryPacket bin = (BinaryPacket) msg;
        byte type = bin.data[0];
        switch (type) {
            case MySQLPacket.COM_INIT_DB:
                // just init the frontend
                source.initDB(bin);
                break;
            case MySQLPacket.COM_QUERY:
                source.query(bin);
                break;
            case MySQLPacket.COM_PING:
                // todo ping , last access time update
                source.ping();
                break;
            case MySQLPacket.COM_QUIT:
                source.close();
                break;
            case MySQLPacket.COM_PROCESS_KILL:
                source.kill(bin.data);
                break;
            case MySQLPacket.COM_STMT_PREPARE:
                // todo prepare支持,参考MyCat
                source.stmtPrepare(bin.data);
                break;
            case MySQLPacket.COM_STMT_EXECUTE:
                source.stmtExecute(bin.data);
                break;
            case MySQLPacket.COM_STMT_CLOSE:
                source.stmtClose(bin.data);
                break;
            case MySQLPacket.COM_HEARTBEAT:
                source.heartbeat(bin.data);
                break;
            default:
                source.writeErrMessage(ErrorCode.ER_UNKNOWN_COM_ERROR, "Unknown command");
                break;
        }
    }
}
