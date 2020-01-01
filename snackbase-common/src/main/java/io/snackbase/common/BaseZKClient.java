package io.snackbase.common;

import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static io.snackbase.common.registrants.BaseZKPathRegistrants.DATA_NODE_BASE_PATH;

/**
 * {@link ZooKeeper}
 * {@link org.apache.zookeeper.Watcher}
 * {@link }
 *
 * @author maxuefeng
 * @since 2019/12/7
 */
public class BaseZKClient {

    private static Logger logger = LoggerFactory.getLogger(BaseZKClient.class);

    private static ZooKeeper zkClient = null;

    public static ZooKeeper zkClient() throws IOException {
        synchronized (BaseZKClient.class) {
            if (zkClient == null) {
                zkClient = new ZooKeeper(
                        "192.168.74.136:2181",
                        15000,
                        event -> {
                            logger.info("Base zookeeper client Watch type {} ", event.getType());
                            try {
                                zkClient.getChildren(DATA_NODE_BASE_PATH, true);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
            }
            return zkClient;
        }
    }


}
