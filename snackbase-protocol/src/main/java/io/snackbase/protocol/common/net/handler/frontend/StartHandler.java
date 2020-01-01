/*
 * Copyright (C) 2016 alchemystar, Inc. All Rights Reserved.
 */
package io.snackbase.protocol.common.net.handler.frontend;

import io.snackbase.protocol.common.net.proto.util.ErrorCode;
import io.snackbase.protocol.parser.ServerParse;
import io.snackbase.protocol.parser.ServerParseStart;

/**
 * StartHandler
 *
 * @Author lizhuyang
 */
public final class StartHandler {

    public static void handle(String stmt, FrontendConnection c, int offset) {
        switch (ServerParseStart.parse(stmt, offset)) {
            case ServerParseStart.TRANSACTION:
                c.writeErrMessage(ErrorCode.ER_UNKNOWN_COM_ERROR, "Unsupported statement");
                break;
            default:
                // todo data source
                  c.execute(stmt, ServerParse.START);
                break;
        }
    }

}