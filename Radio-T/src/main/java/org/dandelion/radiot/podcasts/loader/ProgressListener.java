package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.podcasts.core.ErrorListener;

public interface ProgressListener extends ErrorListener {
    void onStarted();
    void onFinished();

    public static ProgressListener Null = new ProgressListener() {
        @Override
        public void onStarted() {
        }

        @Override
        public void onFinished() {
        }

        @Override
        public void onError(String errorMessage) {
        }
    };
}
