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
import io.snackbase.protocol.common.net.proto.util.LongUtil;
import io.snackbase.protocol.common.net.proto.util.PacketUtil;
import io.snackbase.protocol.parser.util.ParseUtil;

/**
 * SelectLastInsertId
 *
 * @Author lizhuyang
 */
public class SelectLastInsertId {

    private static final String ORG_NAME = "LAST_INSERT_ID()";
    private static final int FIELD_COUNT = 1;
    private static final ResultSetHeaderPacket header = PacketUtil.getHeader(FIELD_COUNT);
    static {
        byte packetId = 0;
        header.packetId = ++packetId;
    }

    public static void response(FrontendConnection c, String stmt, int aliasIndex) {
        ChannelHandlerContext ctx = c.getCtx();
        String alias = ParseUtil.parseAlias(stmt, aliasIndex);
        if (alias == null) {
            alias = ORG_NAME;
        }

        ByteBuf buffer = ctx.alloc().buffer();

        // write header
        buffer = header.writeBuf(buffer, ctx);

        // write fields
        byte packetId = header.packetId;
        FieldPacket field = PacketUtil.getField(alias, ORG_NAME, Fields.FIELD_TYPE_LONGLONG);
        field.packetId = ++packetId;
        buffer = field.writeBuf(buffer, ctx);

        // write eof
        EOFPacket eof = new EOFPacket();
        eof.packetId = ++packetId;
        buffer = eof.writeBuf(buffer, ctx);

        // write rows
        RowDataPacket row = new RowDataPacket(FIELD_COUNT);
        row.add(LongUtil.toBytes(c.getLastInsertId()));
        row.packetId = ++packetId;
        buffer = row.writeBuf(buffer, ctx);

        // write last eof
        EOFPacket lastEof = new EOFPacket();
        lastEof.packetId = ++packetId;
        buffer = lastEof.writeBuf(buffer, ctx);

        // post write
        ctx.writeAndFlush(buffer);
    }

}