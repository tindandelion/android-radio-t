package org.dandelion.radiot.helpers;

import junit.framework.Assert;
import org.dandelion.radiot.podcasts.download.NotificationManager;

import java.io.File;

public class FakeNotificationManager implements NotificationManager {
    private SyncValueHolder<NotificationParams> paramsHolder = new SyncValueHolder<NotificationParams>();

    @Override
    public void showSuccess(String title, File audioFile) {
        paramsHolder.setValue(new NotificationParams(true, title, audioFile, 0));
    }

    @Override
    public void showError(String title, int errorCode) {
        paramsHolder.setValue(new NotificationParams(false, title, null, errorCode));
    }

    public void assertShowsSuccess(String title, File audioFile) {
        NotificationParams params = paramsHolder.getValue();
        Assert.assertTrue("Success", params.success);
        Assert.assertEquals("Title", title, params.title);
        Assert.assertEquals("Path", audioFile, params.path);
    }

    public void assertShowsError(String title, long errorCode) {
        NotificationParams params = paramsHolder.getValue();
        Assert.assertFalse("Success", params.success);
        Assert.assertEquals("Title", title, params.title);
        Assert.assertEquals("Error Code", errorCode, params.errorCode);
    }

    private static class NotificationParams {
        public String title;
        public File path;
        public boolean success;
        public long errorCode;

        private NotificationParams(boolean success, String title, File path, long errorCode) {
            this.success = success;
            this.title = title;
            this.path = path;
            this.errorCode = errorCode;
        }
    }
}
