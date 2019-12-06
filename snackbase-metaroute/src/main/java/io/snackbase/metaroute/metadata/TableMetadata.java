package io.snackbase.metaroute.metadata;

import io.jopen.snack.common.TableInfo;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author maxuefeng
 * @see TableInfo
 * @since 2019/12/6
 */
public class TableMetadata implements Metadata {
    //
    
    // storage table info
    public Set<TableInfo> tableInfoList = new CopyOnWriteArraySet<>();
    public final static String FILE_SUFFIX = ".table";
}
