/*
 * Copyright (C) 2016 alchemystar, Inc. All Rights Reserved.
 */
package io.snackbase.protocol.common.net.response;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.snackbase.protocol.common.net.handler.frontend.FrontendConnection;
import io.snackbase.protocol.common.net.proto.mysql.EOFPacket;
import io.snackbase.protocol.common.net.proto.mysql.FieldPacket;
import io.snackbase.protocol.common.net.proto.mysql.ResultSetHeaderPacket;
import io.snackbase.protocol.common.net.proto.mysql.RowDataPacket;
import io.snackbase.protocol.common.net.proto.util.Fields;
import io.snackbase.protocol.common.net.proto.util.PacketUtil;
import io.snackbase.protocol.common.net.proto.util.Versions;

/**
 * SelectVersion
 *
 * @Author lizhuyang
 */
public class SelectVersion {

    private static final int FIELD_COUNT = 1;
    private static final ResultSetHeaderPacket header = PacketUtil.getHeader(FIELD_COUNT);
    private static final FieldPacket[] fields = new FieldPacket[FIELD_COUNT];
    private static final EOFPacket eof = new EOFPacket();
    static {
        int i = 0;
        byte packetId = 0;
        header.packetId = ++packetId;
        fields[i] = PacketUtil.getField("VERSION()", Fields.FIELD_TYPE_VAR_STRING);
        fields[i++].packetId = ++packetId;
        eof.packetId = ++packetId;
    }

    public static void response(FrontendConnection c) {
        ChannelHandlerContext ctx = c.getCtx();
        ByteBuf buffer = ctx.alloc().buffer();
        buffer = header.writeBuf(buffer, ctx);
        for (FieldPacket field : fields) {
            buffer = field.writeBuf(buffer, ctx);
        }
        buffer = eof.writeBuf(buffer, ctx);
        byte packetId = eof.packetId;
        RowDataPacket row = new RowDataPacket(FIELD_COUNT);
        row.add(Versions.SERVER_VERSION);
        row.packetId = ++packetId;
        buffer = row.writeBuf(buffer, ctx);
        EOFPacket lastEof = new EOFPacket();
        lastEof.packetId = ++packetId;
        buffer = lastEof.writeBuf(buffer, ctx);
        ctx.writeAndFlush(buffer);
    }

}