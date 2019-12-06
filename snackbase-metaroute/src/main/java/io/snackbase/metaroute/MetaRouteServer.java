package io.snackbase.metaroute;

import com.google.common.base.Joiner;
import com.google.common.io.MoreFiles;
import com.google.protobuf.ByteString;
import io.jopen.json.Json;
import io.jopen.snack.common.serialize.KryoHelper;
import io.snackbase.common.exception.NetWorkUtil;
import io.snackbase.metaroute.metadata.DBMetadata;
import io.snackbase.metaroute.metadata.TableMetadata;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
final class MetaRouteServer implements Watcher {

    private static Logger logger = LoggerFactory.getLogger(MetaRouteServer.class);

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
     * base path
     */
    private final String baseNodePath = "/SnackBase";

    /**
     * TODO
     *
     * @see Configuration
     * <p>
     * storage metadata location
     */
    private final String storageMetadataPath = "data";

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
    private List<ACL> aclList;

    /**
     * storage db metadata
     */
    private DBMetadata dbMetadata;

    /**
     * storage table metadata
     */
    private TableMetadata tableMetadata;

    /**
     * @param zkConnectionString zookeeper connection url address
     * @param aclList            acl list
     * @throws IOException          network io exception
     * @throws KeeperException      keep alive connection
     * @throws InterruptedException thread exception
     * @see org.apache.zookeeper.Watcher
     */
    public MetaRouteServer(String zkConnectionString, List<ACL> aclList) throws IOException, KeeperException, InterruptedException {
        this.aclList = aclList;
        zooKeeper = new ZooKeeper(zkConnectionString, 5000, this);

        // check is exist
        Stat exists = zooKeeper.exists(baseNodePath, System.err::println);
        if (exists != null) {
            zooKeeper.delete(baseNodePath, exists.getVersion());
        }
        // recreate SnackBase zk base path
        else {
            zooKeeper.create(baseNodePath, "SnackBase".getBytes(), null, CreateMode.PERSISTENT);
        }
    }

    public void loadMetadata() throws URISyntaxException, IOException {
        // data dir storage metadata dri
        // first level dir is db metadata
        // second level dir is table metadata
        File metadataDir = new File(this.storageMetadataPath);
        File[] dbDirs = metadataDir.listFiles();
        // an dir and {name}.db is an database object
        if (dbDirs != null && dbDirs.length > 0) {
            for (File dbDir : dbDirs) {
                if (dbDir.isDirectory()) {
                    Optional<File> optional = Arrays.stream(dbDirs)
                            .filter(d -> d.getName().endsWith(dbDir.getName() + DBMetadata.FILE_SUFFIX))
                            .findFirst();

                    if (optional.isPresent()) {
                        // load database info data
                        File dbFile = optional.get();
                        // read db file return the byte array
                        byte[] dbBytes = MoreFiles.asByteSource(Paths.get(new URI(dbFile.getAbsolutePath())), StandardOpenOption.WRITE).read();
                        this.dbMetadata = KryoHelper.deserialization(dbBytes, DBMetadata.class);

                        // load table info data
                        File[] tableFiles = dbDir.listFiles((dir, name) -> name.contains(TableMetadata.FILE_SUFFIX));
                        if (tableFiles != null && tableFiles.length > 0) {
                            for (File tableFile : tableFiles) {
                                byte[] tableBytes = MoreFiles.asByteSource(Paths.get(new URI(tableFile.getAbsolutePath())), StandardOpenOption.READ).read();
                                this.tableMetadata = KryoHelper.deserialization(tableBytes, TableMetadata.class);
                            }
                        }
                    }
                }
            }
            // load data completed
        }

    }

    private void registry() throws Exception {
        // current node path
        String thisNodePath = Joiner.on("/").join(new String[]{baseNodePath, Configuration.get("node.id").toString()});
        byte[] thisNodeData = mergeNodeInfo();

        // check exist;if exist pre delete node
        // and create new node
        Stat existsStat = this.zooKeeper.exists(thisNodePath, null);
        if (existsStat != null) {
            this.zooKeeper.delete(thisNodePath, existsStat.getVersion());
        }

        // create data
        String ret = this.zooKeeper.create(
                // node path
                thisNodePath,
                // node data
                thisNodeData,
                // acl
                this.aclList,
                // create mode
                CreateMode.EPHEMERAL);

        logger.info("create base children node result {} ", ret);
    }

    /**
     * a watcher object which will be notified of state changes, may
     * also be notified for node events
     *
     * @param event zookeeper watch
     * @see ZooKeeper#ZooKeeper(String, int, Watcher)
     */
    @Override
    public void process(WatchedEvent event) {
    }

    private byte[] mergeNodeInfo() throws Exception {

        // unique tag
        String mac = NetWorkUtil.getAddress("mac");
        // Intranet ip address
        String intranetIp = NetWorkUtil.getAddress("ip");
        // Extranet ip address
        String extranetIp = String.valueOf(Configuration.get("bind_ip"));
        extranetIp = (extranetIp.equals("null") || StringUtils.isBlank(extranetIp)) ? intranetIp : extranetIp;

        return Json.of("mac", mac, "intranetIp", intranetIp, "extranetIp", extranetIp).toJSONString().getBytes(StandardCharsets.UTF_8);
    }
}
