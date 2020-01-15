package io.snackbase.metaroute.metadata;

import io.jopen.snack.common.TableInfo;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author maxuefeng
 * @see TableInfo
 * @since 2019/12/6
 * @see java.util.concurrent.Future
 */
public class TableMetadata implements Metadata {
    //

    // storage table info
    private Set<TableInfo> tableInfoSet = new CopyOnWriteArraySet<>();

    private String dbName;

    public final static String FILE_SUFFIX = ".table";

    public Set<TableInfo> getTableInfoSet() {
        return tableInfoSet;
    }

    public void setTableInfoSet(Set<TableInfo> tableInfoSet) {
        this.tableInfoSet = tableInfoSet;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}
