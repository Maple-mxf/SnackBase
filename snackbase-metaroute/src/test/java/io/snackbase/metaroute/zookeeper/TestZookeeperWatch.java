package io.snackbase.metaroute.zookeeper;

import com.google.common.base.Joiner;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * {@link org.apache.zookeeper.Watcher}
 *
 * @author maxuefeng
 * @since 2019/12/6
 */
public class TestZookeeperWatch {

    private String zkURL = "192.168.1.109:2181";
    private final String basePath = "/SnackBase";
    // private Logger logger = Logger.getLogger(TestZookeeperWatch.class);
    private Logger logger = LoggerFactory.getLogger(TestZookeeperWatch.class);

    class CommonWatcher implements Watcher {
        @Override
        public void process(WatchedEvent watchedEvent) {
            System.err.println(String.format("path: [ %s ] ", watchedEvent.getPath()));
            System.err.println(String.format("state [ %s ] ", watchedEvent.getState()));
            System.err.println(String.format("event type  [ %s ] ", watchedEvent.getType()));
            System.err.println(String.format("wrapperEvent [ %s ]", watchedEvent.getWrapper()));
        }
    }

    @Test
    public void client1() throws IOException, KeeperException, InterruptedException {
        ZooKeeper client = new ZooKeeper(zkURL, 15000, new CommonWatcher());
        // Stat stat = client.exists(basePath, new CommonWatcher());
        Thread.sleep(10000000);
    }

    @Test
    public void client2() throws IOException, KeeperException, InterruptedException {
        ZooKeeper client = new ZooKeeper(zkURL, 150000, new CommonWatcher());
        client.create(basePath, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    @Test
    public void deleteNode() throws IOException, KeeperException, InterruptedException {
        ZooKeeper client = new ZooKeeper(zkURL, 150000, new CommonWatcher());
        Stat stat = client.exists(basePath, null);
        if (stat != null) {
            client.delete(basePath, stat.getVersion());
        }
    }

    @Test
    public void getNodeData() throws IOException, KeeperException, InterruptedException {
        ZooKeeper client = new ZooKeeper(zkURL, 150000, System.err::println);
        Stat stat = client.exists(basePath, null);
        if (stat == null) {
            String result = client.create(basePath, "SnackBase".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            logger.info("create data node result  " + result);
        }

        // get node version
        stat = client.exists(basePath, null);
        System.err.println(stat.getVersion());

        // get data
        byte[] data = client.getData(basePath, new CommonWatcher(), stat);
        String dataStr = new String(data);
        System.err.println(dataStr);

        // block thread
        Thread.sleep(100000);
    }

    @Test
    public void createChildrenNode() throws IOException, KeeperException, InterruptedException {
        ZooKeeper client = new ZooKeeper(zkURL, 150000, System.err::println);
        String path = client.create(Joiner.on("/").join(new String[]{basePath, "node1"}), "{ip:\"192.168.13.219\"}".getBytes(StandardCharsets.UTF_8),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        logger.info("create children node result {} ", path);
    }

    @Test
    public void getBaseNodeChildrenNode() throws IOException, KeeperException, InterruptedException {
        ZooKeeper client = new ZooKeeper(zkURL, 150000, System.err::println);
        List<String> children = client.getChildren(basePath, new CommonWatcher());
        children.forEach(System.err::println);
        Thread.sleep(10000000);
    }

}
