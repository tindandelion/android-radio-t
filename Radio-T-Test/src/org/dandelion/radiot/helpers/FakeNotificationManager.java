package org.dandelion.radiot.helpers;

import junit.framework.Assert;
import org.dandelion.radiot.podcasts.download.NotificationManager;

public class FakeNotificationManager implements NotificationManager {
    private SyncValueHolder<String> notification = new SyncValueHolder<String>();

    @Override
    public void showNotification(String title) {
        notification.setValue(title);
    }
    
    public void assertShowsNotificationIconFor(String title) {
        Assert.assertEquals("Notification icon", title, notification.getValue());
    }
}
