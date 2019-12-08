package io.snackbase.metaroute;

import com.google.protobuf.ByteString;
import io.snackbase.common.registrants.BaseZKPathRegistrants;
import io.snackbase.metaroute.metadata.DBMetadata;
import io.snackbase.metaroute.metadata.TableMetadata;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * receive client sql and parse sql,when completed parse sql,get all data node
 * info do DSL operation
 * <p>
 * 1 registryRootNodePath the meta route info to zookeeper
 * 2 discovery all meta route(merge all meta route node metadata info)
 * 3 receive the client sql request(IO multiplex/multi thread receive network info)
 * 4 optional security manager(acl function)
 *
 * @author maxuefeng
 * @since 2019/12/5
 */
public
final class MetaRoute implements Watcher {

    //
    private static Logger logger = LoggerFactory.getLogger(MetaRoute.class);

    /**
     * storage all data node info (must be multi thread security)
     * this dataNodeInfoSet will be persistence on disk and synchronize
     * all metaRoute node dataNodeInfoSet(merge the set)
     */
    private final CopyOnWriteArraySet<DataNodeInfo> dataNodeInfoSet = new CopyOnWriteArraySet<>();

    /**
     * zookeeper client
     */
    private ZooKeeper zooKeeper;

    /**
     * nodeID
     */
    private String nodeId;

    /**
     * base node data
     */
    private final byte[] baseNodeData = ByteString.copyFromUtf8("SnackBaseMetaRoute").toByteArray();

    /**
     * zookeeper every node has acl verify(when create node)
     *
     * @see ACL
     */
    @Deprecated
    private List<ACL> aclList;



    /**
     * @param zkConnectionString zookeeper connection url address
     * @param aclList            acl list
     * @throws IOException          network io exception
     * @throws KeeperException      keep alive connection
     * @throws InterruptedException thread exception
     * @see org.apache.zookeeper.Watcher
     */
    public MetaRoute(String zkConnectionString, List<ACL> aclList) throws Exception {
        this.aclList = aclList;
        zooKeeper = new ZooKeeper(zkConnectionString, 5000, this);

        // init base zk path
        BaseZKPathRegistrants baseZKPathRegistrants = new BaseZKPathRegistrants();
        baseZKPathRegistrants.initializationBasePath();

        // load data
        // loadDBMetadata();

        // registry current node info
    }


    /**
     * a Watcher object which will be notified of state changes, may
     * also be notified for node events
     *
     * @param event zookeeper watch
     * @see ZooKeeper#ZooKeeper(String, int, Watcher)
     */
    @Override
    public void process(WatchedEvent event) {
    }


    // zookeeper
}
