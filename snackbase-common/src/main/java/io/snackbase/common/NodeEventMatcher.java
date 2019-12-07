package io.snackbase.common;

import org.apache.zookeeper.WatchedEvent;

/**
 * {@link org.apache.zookeeper.Watcher.Event.EventType}
 *
 * @author maxuefeng
 * @since 2019/12/6
 */
public class NodeEventMatcher {

    private WatchedEvent watchedEvent;

    private NodeEventMatcher(WatchedEvent watchedEvent) {
        this.watchedEvent = watchedEvent;
    }

    public static NodeEventMatcher of(WatchedEvent watchedEvent) {
        return new NodeEventMatcher(watchedEvent);
    }

    public void match() {

    }
}
