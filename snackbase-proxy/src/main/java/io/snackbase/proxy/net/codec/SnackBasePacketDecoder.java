package io.snackbase.proxy.net.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.snackbase.proxy.net.common.ByteUtil;
import io.snackbase.proxy.net.proto.packet.BinaryPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author maxuefeng
 * @since 2019/12/9
 */
class SnackBasePacketDecoder extends ByteToMessageDecoder {
    private static Logger logger = LoggerFactory.getLogger(SnackBasePacketDecoder.class);

    private final int packetHeaderSize = 4;
    private final int maxPacketSize = 16 * 1024 * 1024;

    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 4 bytes:3 length + 1 packetId
        if (in.readableBytes() < packetHeaderSize) {
            return;
        }
        in.markReaderIndex();
        int packetLength = ByteUtil.readUB3(in);
        // 过载保护
        if (packetLength > maxPacketSize) {
            throw new IllegalArgumentException("Packet size over the limit " + maxPacketSize);
        }
        byte packetId = in.readByte();
        if (in.readableBytes() < packetLength) {
            // 半包回溯
            in.resetReaderIndex();
            return;
        }
        BinaryPacket packet = new BinaryPacket();
        packet.packetLength = packetLength;
        packet.packetId = packetId;
        // data will not be accessed any more,so we can use this array safely
        packet.data = in.readBytes(packetLength).array();
        if (packet.data == null || packet.data.length == 0) {
            logger.error("get data errorMessage,packetLength=" + packet.packetLength);
        }
        out.add(packet);
    }
}
