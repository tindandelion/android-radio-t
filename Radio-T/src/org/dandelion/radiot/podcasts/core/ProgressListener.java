package org.dandelion.radiot.podcasts.core;

public interface ProgressListener extends ErrorListener {
    void onStarted();
    void onFinished();
}
