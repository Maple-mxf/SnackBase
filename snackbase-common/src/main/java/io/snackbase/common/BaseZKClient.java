package io.snackbase.common;

import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * {@link ZooKeeper}
 *
 * @author maxuefeng
 * @since 2019/12/7
 */
public class BaseZKClient {

    private static Logger logger = LoggerFactory.getLogger(BaseZKClient.class);

    private static ZooKeeper zkClient = null;

    public  static ZooKeeper zkClient() throws IOException {
        synchronized (BaseZKClient.class) {
            if (zkClient == null) {
                zkClient = new ZooKeeper(
                        Configuration.get("zkURL").toString(),
                        5000,
                        event -> logger.info("Base zookeeper client Watch type {} ", event.getType()));
            }
            return zkClient;
        }
    }


}
