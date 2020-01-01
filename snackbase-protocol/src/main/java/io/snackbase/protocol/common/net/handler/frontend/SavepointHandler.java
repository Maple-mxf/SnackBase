/*
 * Copyright (C) 2016 alchemystar, Inc. All Rights Reserved.
 */
package io.snackbase.protocol.common.net.handler.frontend;


import io.snackbase.protocol.common.net.proto.util.ErrorCode;

/**
 * SavePointHandler
 * @Author lizhuyang
 */
public final class SavepointHandler {

    public static void handle(String stmt, FrontendConnection c) {
        c.writeErrMessage(ErrorCode.ER_UNKNOWN_COM_ERROR, "Unsupported statement");
    }

}
