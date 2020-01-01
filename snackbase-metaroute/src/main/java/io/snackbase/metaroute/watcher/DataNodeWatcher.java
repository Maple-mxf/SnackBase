package io.snackbase.metaroute.watcher;

import io.snackbase.common.BaseZKClient;
import io.snackbase.common.registrants.BaseZKPathRegistrants;
import io.snackbase.common.watcher.Watcher;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
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
public class DataNodeWatcher extends Watcher implements org.apache.zookeeper.Watcher {

    private static Logger logger = LoggerFactory.getLogger(DataNodeWatcher.class);

    /**
     * @see ZooKeeper
     */
    private ZooKeeper zkClient = BaseZKClient.zkClient();

    /**
     * @see ACL
     */
    protected List<ACL> aclList = new ArrayList<>();

    public DataNodeWatcher() throws IOException {
    }

    @Override
    public void Watcher() throws KeeperException, InterruptedException {
        // watch data node
        logger.info("watch data node; path {} ", BaseZKPathRegistrants.DATA_NODE_BASE_PATH);
        zkClient.getChildren(BaseZKPathRegistrants.DATA_NODE_BASE_PATH, this);
    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getType().equals(Event.EventType.NodeDataChanged)) {

        }
    }
}
