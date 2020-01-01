package io.snackbase.common.watcher;

import io.snackbase.common.BaseZKClient;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author maxuefeng
 * @since 2019/12/8
 */
public abstract class Watcher {

    private static Logger logger = LoggerFactory.getLogger(Watcher.class);

    /**
     * @see ZooKeeper
     */
    protected ZooKeeper zkClient = BaseZKClient.zkClient();

    /**
     * @see ACL
     */
    protected List<ACL> aclList = new ArrayList<>();

    protected Watcher() throws IOException {
    }

    public abstract void Watcher() throws IOException, KeeperException, InterruptedException;
}
