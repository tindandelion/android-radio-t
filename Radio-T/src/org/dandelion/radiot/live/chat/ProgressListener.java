package org.dandelion.radiot.live.chat;

public interface ProgressListener {
    void onConnecting();
    void onConnected();
    void onError();
}
