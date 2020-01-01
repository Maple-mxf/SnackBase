package io.snackbase.metaroute.registrants;

import com.google.common.base.Joiner;
import com.google.common.io.MoreFiles;
import io.jopen.snack.common.DatabaseInfo;
import io.jopen.snack.common.TableInfo;
import io.jopen.snack.common.exception.SnackRuntimeException;
import io.jopen.snack.common.serialize.KryoHelper;
import io.snackbase.common.BaseZKClient;
import io.snackbase.common.Configuration;
import io.snackbase.common.registrants.BaseZKPathRegistrants;
import io.snackbase.common.registrants.Registrants;
import io.snackbase.metaroute.metadata.DBMetadata;
import io.snackbase.metaroute.metadata.TableMetadata;
import org.apache.zookeeper.*;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

/**
 * @author maxuefeng
 * @since 2019/12/8
 */
public class DBRegistrants extends Registrants {

    private static Logger logger = LoggerFactory.getLogger(DBRegistrants.class);

    /**
     * storage db metadata
     */
    private DBMetadata dbMetadata = new DBMetadata();

    /**
     * storage table metadata
     */
    private TableMetadata tableMetadata = new TableMetadata();

    // meta data storage path
    private String storageMetadataPath = Configuration.get("storageMetadataPath").toString();

    public DBRegistrants() throws Exception {
        // by load data registry db node level
        loadDBMetadata();
        // init node path
        initializationBasePath();
        // add db node watcher
        addDBNodeWatcher(this.dbMetadata.getDatabaseInfoSet());
        // add table node watcher
        addTableNodeWatcher(this.tableMetadata.getDbName(), this.tableMetadata.getTableInfoSet());
    }

    @Override
    public void initializationBasePath() throws Exception {


        Set<DatabaseInfo> databaseInfoList = this.dbMetadata.getDatabaseInfoSet();
        if (databaseInfoList.size() > 0) {
            for (DatabaseInfo databaseInfo : databaseInfoList) {
                registry(Joiner.on("/").join(new String[]{BaseZKPathRegistrants.DB_BASE_PATH, databaseInfo.getName()}),
                        KryoHelper.serialization(databaseInfo), CreateMode.PERSISTENT_SEQUENTIAL);

            }
        }
    }

    /**
     * @throws URISyntaxException db file or table file not found
     * @throws IOException        read IO exception
     * @see io.snackbase.metaroute.metadata.Metadata
     * @see DBMetadata
     * @see TableMetadata
     */
    private void loadDBMetadata() throws URISyntaxException, IOException {


        // data dir storage metadata dri
        // first level dir is db metadata
        // second level dir is table metadata
        File metadataDir = new File(storageMetadataPath);

        // check exist
        if (!metadataDir.exists()) {

            // maybe throw SecurityException
            boolean createSuccess = metadataDir.mkdirs();

            if (!createSuccess) {
                logger.error("create file {} failure ", storageMetadataPath);
                throw new SnackRuntimeException("create storageMetadataPath failure");
            }
            return;
        }

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
                        byte[] dbBytes = MoreFiles.asByteSource(Paths.get(new URI(dbFile.getAbsolutePath())), StandardOpenOption.READ).read();
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
            // load metadata data completed
            logger.info("load metadata data completed");
        }
    }

    @NonNull
    public DBMetadata getDbMetadata() {
        return dbMetadata;
    }

    public void setDbMetadata(@NonNull DBMetadata dbMetadata) {
        this.dbMetadata = dbMetadata;
    }

    @NonNull
    public TableMetadata getTableMetadata() {
        return tableMetadata;
    }

    public void setTableMetadata(@NonNull TableMetadata tableMetadata) {
        this.tableMetadata = tableMetadata;
    }


    /**
     * @see Watcher
     * watch the db node change
     */
    class DBNodeWatcher implements Watcher {

        @Override
        public void process(WatchedEvent event) {

            String[] pathArray = event.getPath().split("/");
            String dbName = pathArray[pathArray.length - 1];

            DatabaseInfo databaseInfo = DBRegistrants.this.dbMetadata.getDatabaseInfoSet()
                    .stream().filter(db -> db.getName().equals(dbName)).findFirst().get();

            // if delete any node
            if (event.getType().equals(Event.EventType.NodeDeleted)) {

                // delete cache
                DBRegistrants.this.dbMetadata.getDatabaseInfoSet().remove(databaseInfo);

                // delete data on disk
                try {
                    // delete .db file
                    Files.deleteIfExists(Paths.get(new URI(Joiner.on("/").join(new String[]{storageMetadataPath, dbName + DBMetadata.FILE_SUFFIX}))));
                    // delete db dir
                    Files.deleteIfExists(Paths.get(new URI(Joiner.on("/").join(new String[]{storageMetadataPath, dbName}))));

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    logger.error(e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (event.getType().equals(Event.EventType.NodeDataChanged)) {
                DBRegistrants.this.dbMetadata.getDatabaseInfoSet().remove(databaseInfo);
                databaseInfo.setName(dbName);

                //com.google.common.io.Files.readLines()
                File file = new File(Joiner.on("/").join(new String[]{storageMetadataPath, dbName}));
                // file.renameTo(new)
            }
        }
    }

    /**
     * @see Watcher
     * watch the table node change
     */
    class TableNodeWatcher implements Watcher {

        @Override
        public void process(WatchedEvent event) {
            String[] pathArray = event.getPath().split("/");
            String tableName = pathArray[pathArray.length - 1];

            // if delete any node
            if (event.getType().equals(Event.EventType.NodeDeleted)) {

            }
        }
    }

    /**
     * add database node watcher
     */
    private void addDBNodeWatcher(Set<DatabaseInfo> databaseInfoSet) throws IOException, KeeperException, InterruptedException {
        ZooKeeper zkClient = BaseZKClient.zkClient();
        for (DatabaseInfo databaseInfo : databaseInfoSet) {
            zkClient.getChildren(Joiner.on("/").join(new String[]{BaseZKPathRegistrants.DB_BASE_PATH, databaseInfo.getName()}),
                    new DBNodeWatcher());
        }
    }

    private void addTableNodeWatcher(String dbName, Set<TableInfo> tableInfoSet) throws IOException, KeeperException, InterruptedException {
        ZooKeeper zkClient = BaseZKClient.zkClient();
        for (TableInfo tableInfo : tableInfoSet) {
            zkClient.getChildren(Joiner.on("/").join(new String[]{BaseZKPathRegistrants.DB_BASE_PATH, dbName, tableInfo.getName()}),
                    new TableNodeWatcher());
        }

    }
}
