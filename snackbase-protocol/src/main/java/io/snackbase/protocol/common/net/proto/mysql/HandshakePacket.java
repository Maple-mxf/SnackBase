/*
 * Copyright (C) 2016 alchemystar, Inc. All Rights Reserved.
 */
package io.snackbase.protocol.common.net.proto.mysql;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.snackbase.protocol.common.net.proto.MySQLPacket;
import io.snackbase.protocol.common.net.proto.util.BufferUtil;

/**
 * MySQL握手包
 *
 * @author maxuefeng
 */
public class HandshakePacket extends MySQLPacket {

    private static final byte[] FILLER_13 = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    /**
     * <pre>必选字段</pre>
     * 协议版本
     */
    public byte protocolVersion;

    /**
     * <pre>必选字段</pre>
     * 服务端版本
     */
    public byte[] serverVersion;

    /**
     * <pre>必选字段</pre>
     * 线程ID
     */
    public long threadId;

    /**
     * <pre>必选字段</pre>
     * 随机数据
     */
    public byte[] seed;

    /**
     * 服务器性能指标
     */
    public int serverCapabilities;

    /**
     *
     */
    public byte serverCharsetIndex;

    /**
     * 服务器状态
     */
    public int serverStatus;

    /**
     *
     */
    public byte[] restOfScrambleBuff;

    public void read(BinaryPacket bin) {
        packetLength = bin.packetLength;
        packetId = bin.packetId;
        MySQLMessage mm = new MySQLMessage(bin.data);
        protocolVersion = mm.read();
        serverVersion = mm.readBytesWithNull();
        threadId = mm.readUB4();
        seed = mm.readBytesWithNull();
        serverCapabilities = mm.readUB2();
        serverCharsetIndex = mm.read();
        serverStatus = mm.readUB2();
        mm.move(13);
        restOfScrambleBuff = mm.readBytesWithNull();
    }

    public void write(final ChannelHandlerContext ctx) {

        // default init 256,so it can avoid buff extract
        final ByteBuf buffer = ctx.alloc().buffer();

        BufferUtil.writeUB3(buffer, calcPacketSize());
        buffer.writeByte(packetId);
        buffer.writeByte(protocolVersion);
        BufferUtil.writeWithNull(buffer, serverVersion);
        BufferUtil.writeUB4(buffer, threadId);
        BufferUtil.writeWithNull(buffer, seed);
        BufferUtil.writeUB2(buffer, serverCapabilities);
        buffer.writeByte(serverCharsetIndex);
        BufferUtil.writeUB2(buffer, serverStatus);
        buffer.writeBytes(FILLER_13);
        // buffer.position(buffer.position() + 13);
        BufferUtil.writeWithNull(buffer, restOfScrambleBuff);
        // just io , so we don't use thread pool
        ctx.writeAndFlush(buffer);

    }

    @Override
    public int calcPacketSize() {
        int size = 1;
        size += serverVersion.length;// n
        size += 5;// 1+4
        size += seed.length;// 8
        size += 19;// 1+2+1+2+13
        size += restOfScrambleBuff.length;// 12
        size += 1;// 1
        return size;
    }


    @Override
    protected String getPacketInfo() {
        return "MySQL Handshake Packet";
    }
}
