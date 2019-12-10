/*
 * Copyright (C) 2016 alchemystar, Inc. All Rights Reserved.
 */
package io.snackbase.proxy.common.net.handler.factory;

import io.snackbase.proxy.common.config.SystemConfig;
import io.snackbase.proxy.common.net.handler.backend.pool.MySqlDataSource;
import io.snackbase.proxy.common.net.handler.frontend.FrontendConnection;
import io.snackbase.proxy.common.net.handler.frontend.ServerQueryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * FrontendConnection 工厂类
 * @Author lizhuyang
 */
public class FrontConnectionFactory {

    private static final Logger logger = LoggerFactory.getLogger(FrontConnectionFactory.class);

    private final MySqlDataSource dataSource;

    public FrontConnectionFactory(MySqlDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * MySql ThreadId Generator
     */
    private static final AtomicInteger ACCEPT_SEQ = new AtomicInteger(0);

    public FrontendConnection getConnection() {
        FrontendConnection connection = new FrontendConnection();
        connection.setDataSource(dataSource);
        connection.setQueryHandler(new ServerQueryHandler(connection));
        connection.setId(ACCEPT_SEQ.getAndIncrement());
        logger.info("connection Id="+connection.getId());
        connection.setCharset(SystemConfig.DEFAULT_CHARSET);
        connection.setTxIsolation(SystemConfig.DEFAULT_TX_ISOLATION);
        return connection;
    }
}
