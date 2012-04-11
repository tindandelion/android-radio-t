package org.dandelion.radiot.live.core;

import java.io.IOException;

public interface AudioStream {
    void setStateListener(Listener listener);
    void play() throws IOException;
    void stop();
    void release();

    public interface Listener {
        void onStarted();
        void onError();
        void onStopped();
    }
}
