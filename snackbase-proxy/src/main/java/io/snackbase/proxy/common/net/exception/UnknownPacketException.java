/*
 * Copyright (C) 2016 alchemystar, Inc. All Rights Reserved.
 */
package io.snackbase.proxy.common.net.exception;

/**
 * 未知数据包异常
 * 
 * @author lizhuyang
 */
public class UnknownPacketException extends RuntimeException {
    private static final long serialVersionUID = 3152986441780514147L;

    public UnknownPacketException() {
        super();
    }

    public UnknownPacketException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownPacketException(String message) {
        super(message);
    }

    public UnknownPacketException(Throwable cause) {
        super(cause);
    }

}
