package io.snackbase.metaroute;

import io.jopen.snack.common.DatabaseInfo;
import io.jopen.snack.common.TableInfo;

import java.io.Serializable;
import java.util.List;

/**
 * storage every data node base info
 * 1 ip address
 * 2 database info->{
 * tables info->{
 * scheme info
 * }
 * }
 *
 * @author maxuefeng
 * @since 2019/12/5
 */
public final
class DataNodeInfo implements Serializable {
    private String ip;
    private MetaInfo metaInfo;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public MetaInfo getMetaInfo() {
        return metaInfo;
    }

    public void setMetaInfo(MetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }

    public static class MetaInfo {
        private List<DatabaseInfo> databaseInfoList;
        private List<TableInfo> tableInfoList;

        public List<DatabaseInfo> getDatabaseInfoList() {
            return databaseInfoList;
        }

        public void setDatabaseInfoList(List<DatabaseInfo> databaseInfoList) {
            this.databaseInfoList = databaseInfoList;
        }

        public List<TableInfo> getTableInfoList() {
            return tableInfoList;
        }

        public void setTableInfoList(List<TableInfo> tableInfoList) {
            this.tableInfoList = tableInfoList;
        }

        @Override
        public String toString() {
            return "MetaInfo{" +
                    "databaseInfoList=" + databaseInfoList +
                    ", tableInfoSet=" + tableInfoList +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "DataNodeInfo{" +
                "ip='" + ip + '\'' +
                ", metaInfo=" + metaInfo +
                '}';
    }
}
