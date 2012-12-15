package org.dandelion.radiot.helpers.async;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import java.util.ArrayList;
import java.util.List;

public class NotificationTrace<T> {
    private static int DEFAULT_TIMEOUT_MS = 5000;

    private final Object traceLock = new Object();
    private final List<T> trace = new ArrayList<T>();
    private long timeoutMs;

    public NotificationTrace(int timeoutMs) {
        this.timeoutMs = timeoutMs;
    }

    public NotificationTrace() {
        this(DEFAULT_TIMEOUT_MS);
    }

    public void append(T message) {
        synchronized (traceLock) {
            trace.add(message);
            traceLock.notifyAll();
        }
    }

    public void receivedNotification(Matcher<? super T> criteria) throws InterruptedException {
        if (!containsNotification(criteria)) {
            throw new AssertionError(failureDescriptionFrom(criteria));
        }
    }

    public void notReceivedNotification(Matcher<? super T> criteria) throws InterruptedException {
        if (containsNotification(criteria)) {
            throw new AssertionError(failureDescriptionFrom(criteria));
        }
    }

    private boolean containsNotification(Matcher<? super T> criteria) throws InterruptedException {
        Timeout timeout = new Timeout(timeoutMs);
        synchronized (traceLock) {
            NotificationStream<T> stream = new NotificationStream<T>(trace, criteria);
            while(!stream.hasMatched()) {
                if (timeout.hasTimedOut()) {
                    return false;
                }
                timeout.waitOn(traceLock);
            }
        }
        return true;
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

}
