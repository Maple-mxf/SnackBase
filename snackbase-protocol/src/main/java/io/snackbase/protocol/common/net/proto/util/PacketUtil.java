/*
 * Copyright (C) 2016 alchemystar, Inc. All Rights Reserved.
 */
package io.snackbase.protocol.common.net.proto.util;

import io.snackbase.protocol.common.net.proto.mysql.BinaryPacket;
import io.snackbase.protocol.common.net.proto.mysql.ErrorPacket;
import io.snackbase.protocol.common.net.proto.mysql.FieldPacket;
import io.snackbase.protocol.common.net.proto.mysql.ResultSetHeaderPacket;

import java.io.UnsupportedEncodingException;

/**
 * PacketUtil
 *
 * @Author lizhuyang
 */
public class PacketUtil {
    private static final String CODE_PAGE_1252 = "Cp1252";

    public static final ResultSetHeaderPacket getHeader(int fieldCount) {
        ResultSetHeaderPacket packet = new ResultSetHeaderPacket();
        packet.packetId = 1;
        packet.fieldCount = fieldCount;
        return packet;
    }

    public static byte[] encode(String src, String charset) {
        if (src == null) {
            return null;
        }
        try {
            return src.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            return src.getBytes();
        }
    }

    public static final FieldPacket getField(String name, String orgName, int type) {
        FieldPacket packet = new FieldPacket();
        packet.charsetIndex = CharsetUtil.getIndex(CODE_PAGE_1252);
        packet.name = encode(name, CODE_PAGE_1252);
        packet.orgName = encode(orgName, CODE_PAGE_1252);
        packet.type = (byte) type;
        return packet;
    }

    public static final FieldPacket getField(String name, int type) {
        FieldPacket packet = new FieldPacket();
        packet.charsetIndex = CharsetUtil.getIndex(CODE_PAGE_1252);
        packet.name = encode(name, CODE_PAGE_1252);
        packet.type = (byte) type;
        return packet;
    }

    public static final ErrorPacket getShutdown() {
        ErrorPacket error = new ErrorPacket();
        error.packetId = 1;
        error.errno = ErrorCode.ER_SERVER_SHUTDOWN;
        error.message = "The server has been shutdown".getBytes();
        return error;
    }

    public static final FieldPacket getField(BinaryPacket src, String fieldName) {
        FieldPacket field = new FieldPacket();
        field.read(src);
        field.name = encode(fieldName, CODE_PAGE_1252);
        field.packetLength = field.calcPacketSize();
        return field;
    }

}