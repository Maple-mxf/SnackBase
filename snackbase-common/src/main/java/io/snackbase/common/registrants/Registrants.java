package io.snackbase.common.registrants;

import io.snackbase.common.BaseZKClient;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author maxuefeng
 * @since 2019/12/7
 */
public class Registrants {

    /**
     * @see ZooKeeper
     */
    protected ZooKeeper zkClient = BaseZKClient.zkClient();

    /**
     * @see ACL
     */
    protected List<ACL> aclList = new ArrayList<>();

    public Registrants() throws IOException {
    }
}
