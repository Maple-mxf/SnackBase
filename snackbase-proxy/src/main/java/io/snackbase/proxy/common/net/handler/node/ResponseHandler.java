/*
 * Copyright (C) 2016 alchemystar, Inc. All Rights Reserved.
 */
package io.snackbase.proxy.common.net.handler.node;


import io.snackbase.proxy.common.net.proto.mysql.BinaryPacket;
import io.snackbase.proxy.common.net.route.RouteResultset;

import java.util.List;

/**
 * ResponseHandler
 *
 * @Author lizhuyang
 */
public interface ResponseHandler {
    
    // 执行sql
    void execute(RouteResultset rrs);

    // fieldListResponse
    void fieldListResponse(List<BinaryPacket> fieldList);

    // errorResponse
    void errorResponse(BinaryPacket bin);

    // okResponse
    void okResponse(BinaryPacket bin);

    // rowRespons
    void rowResponse(BinaryPacket bin);

    // lastEofResponse
    void lastEofResponse(BinaryPacket bin);
}
