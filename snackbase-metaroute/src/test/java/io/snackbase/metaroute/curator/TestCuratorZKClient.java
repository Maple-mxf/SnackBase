/*
package io.snackbase.metaroute.curator;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;
import java.util.Optional;

*/
/**
 * @author maxuefeng
 * @since 2019/12/5
 *//*

public class TestCuratorZKClient {

    private String connectionString = "114.67.245.63:2181";
    private CuratorFramework curatorClient = null;

    */
/**
     * @see org.apache.curator.framework.imps.CuratorFrameworkImpl
     *//*

    @Before
    public void before() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(500, 3);
        curatorClient = CuratorFrameworkFactory.newClient(connectionString, retryPolicy);
        curatorClient.start();
    }

    */
/**
     * @throws Exception client exception
     * @see Stat
     *//*

    @Test
    public void baseApi() throws Exception {
        // if not exist; will be return null
        Stat stat = curatorClient.checkExists().forPath("/SnackBase");
        System.err.println(stat);
        if (stat == null) {
            String path = curatorClient.create().forPath("/SnackBase");
            System.err.println(path);
        }
    }
}
*/
