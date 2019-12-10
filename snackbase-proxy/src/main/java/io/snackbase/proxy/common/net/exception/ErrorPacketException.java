package io.snackbase.proxy.common.net.exception;

/**
 * ErrorPacketException
 */
public class ErrorPacketException extends RuntimeException {

    public ErrorPacketException() {
        super();
    }

    public ErrorPacketException(String message, Throwable cause) {
        super(message, cause);
    }

    public ErrorPacketException(String message) {
        super(message);
    }

    public ErrorPacketException(Throwable cause) {
        super(cause);
    }

}

