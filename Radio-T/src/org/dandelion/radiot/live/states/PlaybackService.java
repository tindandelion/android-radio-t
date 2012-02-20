package org.dandelion.radiot.live.states;

public interface PlaybackService {
    void switchToNewState(PlaybackState newState);

    void goForeground(int stringId);

    void goBackground();

    void runAsynchronously(Runnable runnable);

    void unscheduleTimeout();

    void scheduleTimeout(int waitTimeout);

    void lockWifi();

    void unlockWifi();
}
