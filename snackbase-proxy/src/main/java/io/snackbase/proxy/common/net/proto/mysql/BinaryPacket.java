/*
 * Copyright (C) 2016 alchemystar, Inc. All Rights Reserved.
 */
package io.snackbase.proxy.common.net.proto.mysql;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.snackbase.proxy.common.net.proto.MySQLPacket;
import io.snackbase.proxy.common.net.proto.util.BufferUtil;

/**
 * MySql包 外层结构
 *
 * @Author lizhuyang
 */
public class BinaryPacket extends MySQLPacket {
    public static final byte OK = 1;
    public static final byte ERROR = 2;
    public static final byte HEADER = 3;
    public static final byte FIELD = 4;
    public static final byte FIELD_EOF = 5;
    public static final byte ROW = 6;
    public static final byte PACKET_EOF = 7;

    public byte[] data;

    @Override
    public int calcPacketSize() {
        return data == null ? 0 : data.length;
    }

    @Override
    public void write(ChannelHandlerContext ctx) {
        ByteBuf byteBuf = ctx.alloc().buffer();
        BufferUtil.writeUB3(byteBuf, packetLength);
        byteBuf.writeByte(packetId);
        byteBuf.writeBytes(data);
        ctx.writeAndFlush(byteBuf);
    }

    @Override
    protected String getPacketInfo() {
        return "MySQL Binary Packet";
    }
}
