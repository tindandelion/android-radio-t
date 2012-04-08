package org.dandelion.radiot.live.service;

public interface NotificationBar {
    void showIcon(int id, int resourceId, String title, String text);
    void hideIcon(int id);
}
