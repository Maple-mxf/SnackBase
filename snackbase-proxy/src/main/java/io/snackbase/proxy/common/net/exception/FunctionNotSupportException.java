package io.snackbase.proxy.common.net.exception;

/**
 * FunctionNotSupportException
 */
public class FunctionNotSupportException extends  RuntimeException {

    public FunctionNotSupportException() {
        super();
    }

    public FunctionNotSupportException(String message) {
        super(message);
    }

    public FunctionNotSupportException(String message, Throwable cause) {
        super(message, cause);
    }

    public FunctionNotSupportException(Throwable cause) {
        super(cause);
    }

    protected FunctionNotSupportException(String message, Throwable cause, boolean enableSuppression,
                                          boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
