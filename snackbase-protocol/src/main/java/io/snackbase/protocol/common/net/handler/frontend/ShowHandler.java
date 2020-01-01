/*
 * Copyright (C) 2016 alchemystar, Inc. All Rights Reserved.
 */
package io.snackbase.protocol.common.net.handler.frontend;


import io.snackbase.protocol.common.net.response.ShowDatabases;
import io.snackbase.protocol.parser.ServerParse;
import io.snackbase.protocol.parser.ServerParseShow;

/**
 * ShowHandler
 * @Author lizhuyang
 */
public final class ShowHandler {
    public static void handle(String stmt, FrontendConnection c, int offset) {
        switch (ServerParseShow.parse(stmt, offset)) {
            case ServerParseShow.DATABASES:
                ShowDatabases.response(c);
                break;
            default:
              // todo datasource
                c.execute(stmt, ServerParse.SHOW);
                break;
        }
    }
}