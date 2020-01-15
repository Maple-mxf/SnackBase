package io.snackbase.protocol.common.net.proto.util;

/**
 * @author maxuefeng
 */
public interface Versions {
    /**
     * 协议版本
     */
    byte PROTOCOL_VERSION = 10;

    /**
     * 服务器版本
     */
    byte[] SERVER_VERSION = "5.1.1-SnackBase".getBytes();
}
