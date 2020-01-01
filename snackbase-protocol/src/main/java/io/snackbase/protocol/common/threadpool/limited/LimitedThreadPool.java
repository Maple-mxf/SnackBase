/*
 * Copyright (C) 2016 alchemystar, Inc. All Rights Reserved.
 */
package io.snackbase.protocol.common.threadpool.limited;


import io.snackbase.protocol.common.threadpool.AbortPolicyWithReport;
import io.snackbase.protocol.common.threadpool.AbstractThreadPool;
import io.snackbase.protocol.common.threadpool.NamedThreadFactory;
import io.snackbase.protocol.common.threadpool.config.ThreadPoolConfig;

import java.util.concurrent.*;

/**
 * 此线程池一直增长，直到上限，增长后不收缩
 *
 * @Author lizhuyang
 */
public class LimitedThreadPool extends AbstractThreadPool {

    @Override
    public Executor getExecutor(ThreadPoolConfig threadPoolConfig) {
        return getExecutor(threadPoolConfig.getName(), threadPoolConfig.getCoreSize(), threadPoolConfig.getMaxSize(),
                threadPoolConfig.getQueues());
    }

    /**
     * @param name     线程前缀
     * @param coreSize 核心线程数量
     * @param maxSize  最大线程数量
     * @param queues   最大排队数量
     *
     * @return
     */
    public Executor getExecutor(String name, int coreSize, int maxSize, int queues) {

        return new ThreadPoolExecutor(coreSize, maxSize, Long.MAX_VALUE, TimeUnit.MILLISECONDS,
                queues == 0 ? new SynchronousQueue<Runnable>() :
                        (queues < 0 ? new LinkedBlockingQueue<Runnable>()
                                 : new LinkedBlockingQueue<Runnable>(queues)),
                new NamedThreadFactory(name, true), new AbortPolicyWithReport(name));
    }
}
