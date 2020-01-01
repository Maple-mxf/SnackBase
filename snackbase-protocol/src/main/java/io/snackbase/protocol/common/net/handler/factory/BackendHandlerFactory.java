/*
 * Copyright (C) 2016 alchemystar, Inc. All Rights Reserved.
 */
package io.snackbase.protocol.common.net.handler.factory;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.snackbase.protocol.common.net.codec.MySqlPacketDecoder;
import io.snackbase.protocol.common.net.handler.backend.BackendAuthenticator;
import io.snackbase.protocol.common.net.handler.backend.BackendConnection;
import io.snackbase.protocol.common.net.handler.backend.BackendHeadHandler;
import io.snackbase.protocol.common.net.handler.backend.BackendTailHandler;

/**
 * 后端连接工厂类
 *
 * @Author lizhuyang
 */
public class BackendHandlerFactory extends ChannelInitializer<SocketChannel> {

    private BackendConnectionFactory factory;

    public BackendHandlerFactory(BackendConnectionFactory factory) {
        this.factory = factory;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        BackendConnection connection = factory.getConnection();
        BackendHeadHandler firstHandler = new BackendHeadHandler(connection);
        BackendAuthenticator authHandler = new BackendAuthenticator(connection);
        BackendTailHandler tailHandler = new BackendTailHandler(connection);
        ch.pipeline().addLast(new MySqlPacketDecoder());
        ch.pipeline().addLast("BackendHeadHandler", firstHandler);
        ch.pipeline().addLast(authHandler);
        ch.pipeline().addLast(tailHandler);
    }
}
