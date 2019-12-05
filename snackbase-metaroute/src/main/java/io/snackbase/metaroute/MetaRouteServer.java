package io.snackbase.metaroute;

import com.google.common.base.Preconditions;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.RetryForever;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * receive client sql and parse sql,when completed parse sql,get all data node
 * info do DSL operation
 * <p>
 * 1 registry the meta route info to zookeeper
 * 2 discovery all meta route(merge all meta route node metadata info)
 * 3 receive the client sql request(IO multiplex/multi thread receive network info)
 * 4 optional security manager(acl function)
 *
 * @author maxuefeng
 * @since 2019/12/5
 */
public
final class MetaRouteServer {

    /**
     * storage all data node info (must be multi thread security)
     * this dataNodeInfoSet will be persistence on disk and synchronize
     * all metaRoute node dataNodeInfoSet(merge the set)
     */
    private final CopyOnWriteArraySet<DataNodeInfo> dataNodeInfoSet = new CopyOnWriteArraySet<>();

    /**
     * zookeeper connect url
     */
    private String zkURL;

    /**
     * @see CuratorFrameworkFactory
     * @see org.apache.curator.RetryPolicy
     * zookeeper client
     */
    private CuratorFramework curatorClient;

    private String zkRegistryPath = "/SnackBase/";

    public MetaRouteServer() {
        curatorClient = CuratorFrameworkFactory.newClient(zkURL, new RetryForever(500));
        this.curatorClient.start();
    }


    private void registry() throws Exception {
        Preconditions.checkNotNull(this.curatorClient);
        Preconditions.checkArgument(!this.curatorClient.getState().equals(CuratorFrameworkState.STARTED),
                String.format("curator zookeeper client start fail state %s ", this.curatorClient.getState()));

        this.curatorClient.checkExists().forPath(zkRegistryPath);

    }


    public static void main(String[] args) {

    }
}
