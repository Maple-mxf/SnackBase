package io.snackbase.metaroute.metadata;

import io.jopen.snack.common.DatabaseInfo;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author maxuefeng
 * @see io.jopen.snack.common.DatabaseInfo
 * @since 2019/12/6
 */
public class DBMetadata implements Metadata {
    // storage multi database
    public final Set<DatabaseInfo> databaseInfoList = new CopyOnWriteArraySet<>();
    public final static String FILE_SUFFIX = ".db";
}