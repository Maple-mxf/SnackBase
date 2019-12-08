package io.snackbase.common;

import org.apache.zookeeper.WatchedEvent;

/**
 * {@link org.apache.zookeeper.Watcher.Event.EventType}
 *
 * @author maxuefeng
 * @since 2019/12/6
 */
@Deprecated
public class WatcherEventMatcher {

    private WatchedEvent watchedEvent;

    private WatcherEventMatcher(WatchedEvent watchedEvent) {
        this.watchedEvent = watchedEvent;
    }

    public static WatcherEventMatcher of(WatchedEvent watchedEvent) {
        return new WatcherEventMatcher(watchedEvent);
    }

//    public void match(Consumer<WatchedEvent> consumer, Watcher.Event.EventType.NodeCreated nodeCreated) {
//        consumer.accept(this.watchedEvent);
//    }
}
