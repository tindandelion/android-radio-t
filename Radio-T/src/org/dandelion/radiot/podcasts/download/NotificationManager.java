package org.dandelion.radiot.podcasts.download;

import java.io.File;

public interface NotificationManager {
    void showSuccess(String title, File audioFile);
    void showError(String title);
}
