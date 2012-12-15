package org.dandelion.radiot.helpers.async;

import org.hamcrest.Matcher;

import java.util.List;

class NotificationStream<T> {
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
