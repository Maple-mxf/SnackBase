/*
 * Copyright (C) 2016 alchemystar, Inc. All Rights Reserved.
 */
package io.snackbase.proxy.net.proto.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.snackbase.proxy.net.common.BufferUtil;

/**
 * CommandPacket
 *
 * @Author lizhuyang
 */
public class CommandPacket extends SnackBasePacket {

    public byte command;
    public byte[] arg;

    public void read(byte[] data) {
        MySQLMessage mm = new MySQLMessage(data);
        packetLength = mm.readUB3();
        packetId = mm.read();
        command = mm.read();
        arg = mm.readBytes();
    }


    public ByteBuf getByteBuf(ChannelHandlerContext ctx){
        ByteBuf buffer = ctx.alloc().buffer();
        BufferUtil.writeUB3(buffer, calcPacketSize());
        buffer.writeByte(packetId);
        buffer.writeByte(command);
        buffer.writeBytes(arg);
        return buffer;
    }

    public void write(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(getByteBuf(ctx));
    }

    @Override
    public int calcPacketSize() {
        return 1 + arg.length;
    }

    @Override
    protected String getPacketInfo() {
        return "MySQL Command Packet";
    }

}

