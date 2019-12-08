package io.snackbase.common.registrants;

import io.snackbase.common.BaseZKClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author maxuefeng
 * @since 2019/12/7
 */
public abstract class Registrants {

    private static Logger logger = LoggerFactory.getLogger(Registrants.class);

    /**
     * @see ZooKeeper
     */
    private ZooKeeper zkClient = BaseZKClient.zkClient();

    /**
     * @see ACL
     */
    protected List<ACL> aclList = new ArrayList<>();

    public Registrants() throws IOException {
    }

    public void registry(String path, byte[] data, CreateMode createMode) throws KeeperException, InterruptedException {
        // check is exist
        Stat exists = zkClient.exists(path, System.err::println);
        if (exists == null) {
            zkClient.create(path, data, null, createMode);
        }
        logger.info("check base path complete");
    }

    public abstract void initializationBasePath() throws Exception;
}
