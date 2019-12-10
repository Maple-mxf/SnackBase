package io.snackbase.proxy.common.net.exception;

/**
 */
public class RetryConnectFailException extends RuntimeException {

    public RetryConnectFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public RetryConnectFailException(String message) {
        super(message);
    }
}
