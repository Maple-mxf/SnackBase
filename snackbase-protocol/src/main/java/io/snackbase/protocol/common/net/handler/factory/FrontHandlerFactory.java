/*
 * Copyright (C) 2016 alchemystar, Inc. All Rights Reserved.
 */
package io.snackbase.protocol.common.net.handler.factory;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.snackbase.protocol.common.net.codec.MySqlPacketDecoder;
import io.snackbase.protocol.common.net.handler.backend.pool.MySqlDataSource;
import io.snackbase.protocol.common.net.handler.frontend.FrontendAuthenticator;
import io.snackbase.protocol.common.net.handler.frontend.FrontendConnection;
import io.snackbase.protocol.common.net.handler.frontend.FrontendGroupHandler;
import io.snackbase.protocol.common.net.handler.frontend.FrontendTailHandler;

/**
 * 前端handler工厂
 *
 * @Author lizhuyang
 */
public class FrontHandlerFactory extends ChannelInitializer<SocketChannel> {

    private FrontConnectionFactory factory;

    public FrontHandlerFactory(MySqlDataSource dataSource) {
        factory = new FrontConnectionFactory(dataSource);
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        FrontendConnection source = factory.getConnection();
        FrontendGroupHandler groupHandler = new FrontendGroupHandler(source);
        FrontendAuthenticator authHandler = new FrontendAuthenticator(source);
        FrontendTailHandler tailHandler = new FrontendTailHandler(source);
        // 心跳handler
        //ch.pipeline().addLast(new IdleStateHandler(10, 10, 10));
        // decode mysql packet depend on it's length
        ch.pipeline().addLast(new MySqlPacketDecoder());
        ch.pipeline().addLast(groupHandler);
        ch.pipeline().addLast(authHandler);
        ch.pipeline().addLast(tailHandler);

    }
}