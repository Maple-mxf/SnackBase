/*
 * Copyright (C) 2016 alchemystar, Inc. All Rights Reserved.
 */
package io.snackbase.protocol.common.net.handler.frontend;

import io.snackbase.protocol.common.net.response.*;
import io.snackbase.protocol.parser.ServerParse;
import io.snackbase.protocol.parser.ServerParseSelect;
import io.snackbase.protocol.parser.util.ParseUtil;

/**
 * SelectHandler
 *
 * @Author lizhuyang
 */
public final class SelectHandler {

    public static void handle(String stmt, FrontendConnection c, int offs) {
        int offset = offs;
        switch (ServerParseSelect.parse(stmt, offs)) {
            case ServerParseSelect.VERSION_COMMENT:
                SelectVersionComment.response(c);
                break;
            case ServerParseSelect.DATABASE:
                SelectDatabase.response(c);
                break;
            case ServerParseSelect.USER:
                SelectUser.response(c);
                break;
            case ServerParseSelect.VERSION:
                SelectVersion.response(c);
                break;
            case ServerParseSelect.LAST_INSERT_ID:
                offset = ParseUtil.move(stmt, 0, "select".length());
                loop: for (; offset < stmt.length(); ++offset) {
                    switch (stmt.charAt(offset)) {
                        case ' ':
                            continue;
                        case '/':
                        case '#':
                            offset = ParseUtil.comment(stmt, offset);
                            continue;
                        case 'L':
                        case 'l':
                            break loop;
                    }
                }
                offset = ServerParseSelect.indexAfterLastInsertIdFunc(stmt, offset);
                offset = ServerParseSelect.skipAs(stmt, offset);
                SelectLastInsertId.response(c, stmt, offset);
                break;
            case ServerParseSelect.IDENTITY:
                offset = ParseUtil.move(stmt, 0, "select".length());
                loop: for (; offset < stmt.length(); ++offset) {
                    switch (stmt.charAt(offset)) {
                        case ' ':
                            continue;
                        case '/':
                        case '#':
                            offset = ParseUtil.comment(stmt, offset);
                            continue;
                        case '@':
                            break loop;
                    }
                }
                int indexOfAtAt = offset;
                offset += 2;
                offset = ServerParseSelect.indexAfterIdentity(stmt, offset);
                String orgName = stmt.substring(indexOfAtAt, offset);
                offset = ServerParseSelect.skipAs(stmt, offset);
                SelectIdentity.response(c, stmt, offset, orgName);
                break;
            default:
                c.execute(stmt, ServerParse.SELECT);
                break;
        }
    }

}