package org.dandelion.radiot.helpers;

import junit.framework.Assert;
import org.dandelion.radiot.podcasts.download.NotificationManager;

import java.io.File;

public class FakeNotificationManager implements NotificationManager {
    private SyncValueHolder<NotificationParams> paramsHolder = new SyncValueHolder<NotificationParams>();

    @Override
    public void showNotification(String title, File audioFile) {
        paramsHolder.setValue(new NotificationParams(title, audioFile));
    }
    
    public void assertShowsNotificationIconFor(String title, File audioFile) {
        NotificationParams params = paramsHolder.getValue();
        Assert.assertEquals("Title", title, params.title);
        Assert.assertEquals("Path", audioFile, params.path);
    }
    
    private static class NotificationParams {
        public String title;
        public File path;

        private NotificationParams(String title, File path) {
            this.title = title;
            this.path = path;
        }
    }
}
