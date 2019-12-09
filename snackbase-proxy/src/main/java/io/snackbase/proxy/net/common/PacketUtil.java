package io.snackbase.proxy.net.common;

import io.snackbase.proxy.net.proto.packet.BinaryPacket;
import io.snackbase.proxy.net.proto.packet.ErrorPacket;
import io.snackbase.proxy.net.proto.packet.FieldPacket;
import io.snackbase.proxy.net.proto.packet.ResultSetHeaderPacket;

import java.io.UnsupportedEncodingException;

public class PacketUtil {
    private static final String CODE_PAGE_1252 = "Cp1252";

    public static ResultSetHeaderPacket getHeader(int fieldCount) {
        ResultSetHeaderPacket packet = new ResultSetHeaderPacket();
        packet.packetId = 1;
        packet.fieldCount = fieldCount;
        return packet;
    }

    private static byte[] encode(String src, String charset) {
        if (src == null) {
            return null;
        }
        try {
            return src.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            return src.getBytes();
        }
    }

    public static FieldPacket getField(String name, String orgName, int type) {
        FieldPacket packet = new FieldPacket();
        packet.charsetIndex = CharsetUtil.getIndex(CODE_PAGE_1252);
        packet.name = encode(name, CODE_PAGE_1252);
        packet.orgName = encode(orgName, CODE_PAGE_1252);
        packet.type = (byte) type;
        return packet;
    }

    public static FieldPacket getField(String name, int type) {
        FieldPacket packet = new FieldPacket();
        packet.charsetIndex = CharsetUtil.getIndex(CODE_PAGE_1252);
        packet.name = encode(name, CODE_PAGE_1252);
        packet.type = (byte) type;
        return packet;
    }

    public static ErrorPacket getShutdown() {
        ErrorPacket error = new ErrorPacket();
        error.packetId = 1;
        error.errno = ErrorCode.ER_SERVER_SHUTDOWN;
        error.message = "The server has been shutdown".getBytes();
        return error;
    }

    public static FieldPacket getField(BinaryPacket src, String fieldName) {
        FieldPacket field = new FieldPacket();
        field.read(src);
        field.name = encode(fieldName, CODE_PAGE_1252);
        field.packetLength = field.calcPacketSize();
        return field;
    }

}