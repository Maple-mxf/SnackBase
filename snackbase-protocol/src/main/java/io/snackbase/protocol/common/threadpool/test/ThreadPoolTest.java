/*
 * Copyright (C) 2016 alchemystar, Inc. All Rights Reserved.
 */
package io.snackbase.protocol.common.threadpool.test;

import io.snackbase.protocol.common.threadpool.AbstractThreadPool;
import io.snackbase.protocol.common.threadpool.ThreadPool;
import io.snackbase.protocol.common.threadpool.cached.CachedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ThreadPool测试类
 *
 * @Author lizhuyang
 */
public class ThreadPoolTest {

    protected static final Logger logger = LoggerFactory.getLogger(ThreadPoolTest.class);

    public static void main(String[] args) {

        ThreadPool threadPool = new CachedThreadPool();
        ((AbstractThreadPool)threadPool).getExecutor("ThreadPoolTest");
        logger.info("this is for log test");
    }
}
