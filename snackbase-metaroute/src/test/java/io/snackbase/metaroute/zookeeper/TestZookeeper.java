package io.snackbase.metaroute.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * {@link org.apache.zookeeper.ZooKeeper}
 *
 * @author maxuefeng
 * @since 2019/12/5
 */
public class TestZookeeper {

    private ZooKeeper zkClient;
    private String connectionString = "192.168.74.136:2181";

    @Before
    public void before() throws IOException {
        zkClient = new ZooKeeper(connectionString, 5000, watchedEvent -> System.err.println(watchedEvent.getPath()));
    }

    /**
     * @throws KeeperException
     * @throws InterruptedException
     * @see org.apache.zookeeper.data.ACL
     */
    @Test
    public void baseApi() throws KeeperException, InterruptedException {

        // check is exist
        Stat exists = zkClient.exists("/SnackBase", false);

        // create node by not has acl
        if (exists == null) {
            String ret = zkClient.create("/SnackBase", "SnackBase".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.err.println(ret);
        }

        //
    }
}
