/*
 * Copyright (C) 2016 alchemystar, Inc. All Rights Reserved.
 */
package io.snackbase.proxy.common.threadpool.test;

import alchemystar.lancelot.common.threadpool.AbstractThreadPool;
import alchemystar.lancelot.common.threadpool.ThreadPool;
import alchemystar.lancelot.common.threadpool.cached.CachedThreadPool;
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
