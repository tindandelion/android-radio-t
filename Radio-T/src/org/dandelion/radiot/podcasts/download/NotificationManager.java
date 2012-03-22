package org.dandelion.radiot.podcasts.download;

import java.io.File;

public interface NotificationManager {
    void showNotification(String title, File audioFile);
}
