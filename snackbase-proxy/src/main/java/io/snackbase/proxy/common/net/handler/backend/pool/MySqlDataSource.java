package io.snackbase.proxy.common.net.handler.backend.pool;


import io.snackbase.proxy.common.net.handler.backend.BackendConnection;

/**
 * MySqlDataPool wrapper
 *
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
