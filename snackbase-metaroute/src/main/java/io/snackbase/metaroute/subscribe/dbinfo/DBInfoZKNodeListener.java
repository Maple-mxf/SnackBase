package io.snackbase.metaroute.subscribe.dbinfo;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author maxuefeng
 * @see Watcher
 * @since 2019/12/6
 */
public final
class DBInfoZKNodeListener implements Watcher {

    private static Logger logger = LoggerFactory.getLogger(DBInfoZKNodeListener.class);

    @Override
    public void process(WatchedEvent event) {
        Event.EventType eventType = event.getType();

        //
        if (eventType.equals(Event.EventType.NodeCreated)) {

        } else if (eventType.equals(Event.EventType.NodeDeleted)) {

        } else if (eventType.equals(Event.EventType.NodeDataChanged)) {

        } else if (eventType.equals(Event.EventType.NodeChildrenChanged)) {

        } else if (eventType.equals(Event.EventType.None)) {

        } else {
            logger.error("unknown error");
        }
    }
}
