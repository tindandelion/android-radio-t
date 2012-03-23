package org.dandelion.radiot.helpers;

import junit.framework.Assert;
import org.dandelion.radiot.podcasts.download.NotificationManager;

import java.io.File;

public class FakeNotificationManager implements NotificationManager {
    private SyncValueHolder<NotificationParams> paramsHolder = new SyncValueHolder<NotificationParams>();

    @Override
    public void showSuccess(String title, File audioFile) {
        paramsHolder.setValue(new NotificationParams(true, title, audioFile));
    }

    @Override
    public void showError(String title) {
        paramsHolder.setValue(new NotificationParams(false, title, null));
    }

    public void assertShowsSuccess(String title, File audioFile) {
        NotificationParams params = paramsHolder.getValue();
        Assert.assertTrue("Success", params.success);
        Assert.assertEquals("Title", title, params.title);
        Assert.assertEquals("Path", audioFile, params.path);
    }

    public void assertShowsError(String title) {
        NotificationParams params = paramsHolder.getValue();
        Assert.assertFalse("Success", params.success);
        Assert.assertEquals("Title", title, params.title);
    }

    private static class NotificationParams {
        public String title;
        public File path;
        public boolean success;

        private NotificationParams(boolean success, String title, File path) {
            this.success = success;
            this.title = title;
            this.path = path;
        }
    }
}
