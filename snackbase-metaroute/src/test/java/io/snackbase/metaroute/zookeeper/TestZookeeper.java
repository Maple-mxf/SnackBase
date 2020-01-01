package io.snackbase.metaroute.zookeeper;

import io.snackbase.common.registrants.BaseZKPathRegistrants;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.proto.WatcherEvent;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static io.snackbase.common.registrants.BaseZKPathRegistrants.DATA_NODE_BASE_PATH;

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
        zkClient = new ZooKeeper(connectionString, 1500000, watchedEvent -> {
            try {
                zkClient.getChildren(DATA_NODE_BASE_PATH, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * @throws KeeperException
     * @throws InterruptedException
     * @see org.apache.zookeeper.data.ACL
     */
    @Test
    public void baseApi() throws KeeperException, InterruptedException {

        // check is exist
        Stat exists = zkClient.exists("/SnackBase/test", false);

        // create node by not has acl
        if (exists == null) {
            String ret = zkClient.create("/SnackBase/test", "SnackBase".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.err.println(ret);
        }

        //
    }

    @Test
    public void watchChildrenNode() throws KeeperException, InterruptedException {
        zkClient.getChildren(DATA_NODE_BASE_PATH, event -> {
            System.err.println(event.getPath());
            System.err.println(event.getType());
            WatcherEvent wrapper = event.getWrapper();
            System.err.println();

            try {
                zkClient.getChildren(DATA_NODE_BASE_PATH, true);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        TimeUnit.SECONDS.sleep(100000);
    }

    @Test
    public void testCreateBasePath() throws Exception {
        BaseZKPathRegistrants registrants = new BaseZKPathRegistrants();
        registrants.setAclList(ZooDefs.Ids.OPEN_ACL_UNSAFE);
        registrants.initializationBasePath();
    }

    @Test
    public void createChildren() throws Exception {
        zkClient.create(DATA_NODE_BASE_PATH + "/dataNode1", "192.168.74.136".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    }
}
