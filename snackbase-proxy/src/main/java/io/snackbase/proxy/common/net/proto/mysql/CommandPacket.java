package io.snackbase.proxy.common.net.proto.mysql;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.snackbase.proxy.common.net.proto.MySQLPacket;
import io.snackbase.proxy.common.net.proto.util.BufferUtil;

/**
 * CommandPacket
 *
 * @Author lizhuyang
 */
public class CommandPacket extends MySQLPacket {

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

