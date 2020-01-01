/*
 * Copyright (C) 2016 alchemystar, Inc. All Rights Reserved.
 */
package io.snackbase.protocol.common.net.handler.backend.pool;

import io.snackbase.protocol.common.net.handler.backend.BackendConnection;

/**
 * MySqlDataPool wrapper
 *
 * @Author lizhuyang
 */
public class MySqlDataSource {

    private MySqlDataPool dataPool;

    public MySqlDataSource(MySqlDataPool dataPool) {
        this.dataPool = dataPool;
    }

    public BackendConnection getBackend() {
        return dataPool.getBackend();
    }

    public void recycle(BackendConnection backend){
        dataPool.putBackend(backend);
    }
}
