/*
 * Copyright (C) 2016 alchemystar, Inc. All Rights Reserved.
 */
package io.snackbase.proxy.common.threadpool;

import alchemystar.lancelot.common.threadpool.config.ThreadPoolConfig;

import java.util.concurrent.Executor;

/**
 * @Author lizhuyang
 */
public interface ThreadPool {

    /**
     * 采用默认的线程池配置
     * @param name
     * @return
     */
    public Executor getExecutor(String name);

    /**
     * 使用ThreadPoolConfig配置线程池
     * @param config
     * @return
     */
    public Executor getExecutor(ThreadPoolConfig config);
}
