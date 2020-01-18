package io.snackbase.proxy.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.snackbase.protocol.common.net.codec.MySqlPacketDecoder;
import io.snackbase.protocol.common.net.proto.mysql.BinaryPacket;
import io.snackbase.protocol.common.net.proto.util.ByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * MySQL包解析器
 *
 * @author maxuefeng
 * @since 2019/12/29
 */
public class MySQLPacketDecodeHandler extends ByteToMessageDecoder {

    private static final Logger logger = LoggerFactory.getLogger(MySqlPacketDecoder.class);

    // MySQL每次发送的包的大小不得大于16MB
    private static final int maxPacketSize = 16 * 1024 * 1024;

    /**
     * 参考MySQL官网的包协议
     * <p>
     * MySQL的协议包的
     * <p>
     * MySQL的头包
     */
    private final static int packetHeaderSize = 4;

    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        // 不足以读取  固定大小的包结构来进行消掉粘包问题
        if (in.readableBytes() < packetHeaderSize) {
            return;
        }

        // 标记当前读到的readerIndex
        in.markReaderIndex();

        // 负载保护(MySQL的包不得大于16MB)
        int packetLength = ByteUtil.readUB3(in);

        // 包的大小超过了16MB
        if (packetLength > maxPacketSize) {
            throw new IllegalArgumentException("Packet size over the limit " + maxPacketSize);
        }

        //
        if (packetLength == 0) {
            throw new IllegalArgumentException("body length require gt zero");
        }

        byte packetId = in.readByte();

        byte[] data = in.readBytes(packetLength).array();

        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("body length require gt zero");
        }

        //
        if (in.readableBytes() < packetLength) {
            // 半包回溯 （等待下一次包的长度足够时）
            in.resetReaderIndex();
            return;
        }

        BinaryPacket binaryPacket = new BinaryPacket();
        binaryPacket.packetId = packetId;

        // 读取Body体
        binaryPacket.data = data;

        // 添加数据
        out.add(binaryPacket);
    }
}
