/*
 * Copyright (C) 2016 alchemystar, Inc. All Rights Reserved.
 */
package io.snackbase.protocol.common.net.handler.backend;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Backend Tail Handler
 * @Author lizhuyang
 */
public class BackendTailHandler extends ChannelHandlerAdapter{

    private static final Logger logger = LoggerFactory.getLogger(BackendTailHandler.class);

    private BackendConnection source;

    public BackendTailHandler(BackendConnection source) {
        this.source = source;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("backend exception caught",cause);
        // discard and close
        source.discard();
    }
}
