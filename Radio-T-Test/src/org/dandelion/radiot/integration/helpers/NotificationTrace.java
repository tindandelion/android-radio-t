package org.dandelion.radiot.integration.helpers;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import java.util.ArrayList;
import java.util.List;

public class NotificationTrace<T> {
    private static int DEFAULT_TIMEOUT_MS = 5000;
    private final Object traceLock = new Object();
    private final List<T> trace = new ArrayList<T>();
    private long timeoutMs = DEFAULT_TIMEOUT_MS;

    public void append(T message) {
        synchronized (traceLock) {
            trace.add(message);
            traceLock.notifyAll();
        }
    }

    public void containsNotification(Matcher<? super T> criteria) throws InterruptedException {
        NotificationTimeout timeout = new NotificationTimeout(timeoutMs);
        synchronized (traceLock) {
            NotificationStream<T> stream = new NotificationStream<T>(trace, criteria);
            while(!stream.hasMatched()) {
                if (timeout.hasTimedOut()) {
                    throw new AssertionError(failureDescriptionFrom(criteria));
                }
                timeout.waitOn(traceLock);
            }
        }
    }

    private String failureDescriptionFrom(Matcher<? super T> criteria) {
        StringDescription description = new StringDescription();

        description.appendText("no message matching ")
                .appendDescriptionOf(criteria)
                .appendText(" received within " + timeoutMs + "ms\n");

        if (trace.isEmpty()) {
            description.appendText("received nothing");
        } else {
            description.appendValueList("received:\n   ", "\n   ", "", trace);
        }

        return description.toString();
    }

    static class NotificationStream<T> {
        private List<T> notifications;
        private Matcher<? super T> criteria;
        private int next = 0;

        public NotificationStream(List<T> notifications, Matcher<? super T> criteria) {
            this.notifications = notifications;
            this.criteria = criteria;

        }

        public boolean hasMatched() {
            while (next < notifications.size()) {
                if (criteria.matches(notifications.get(next)))
                    return true;
                next++;
            }
            return false;
        }
    }

    static class NotificationTimeout {
        private final long endTime;

        public NotificationTimeout(long duration) {
            this.endTime = System.currentTimeMillis() + duration;
        }

        public boolean hasTimedOut() {
            return timeRemaining() <= 0;
        }

        public void waitOn(Object lock) throws InterruptedException {
            long waitTime = timeRemaining();
            if (waitTime > 0) lock.wait(waitTime);
        }

        private long timeRemaining() {
            return endTime - System.currentTimeMillis();
        }
    }
}
