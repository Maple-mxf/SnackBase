/*
 * Copyright (C) 2016 alchemystar, Inc. All Rights Reserved.
 */
package io.snackbase.protocol.common.net.exception;

/**
 * @Author lizhuyang
 */
public class RetryConnectFailException extends RuntimeException {

    public RetryConnectFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public RetryConnectFailException(String message) {
        super(message);
    }
}
