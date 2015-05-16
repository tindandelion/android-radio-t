package org.dandelion.radiot.http;

public interface DataMonitor<T> {
    void setProgressListener(ProgressListener listener);
    void setDataConsumer(Consumer<T> consumer);
    void setErrorConsumer(Consumer<Exception> consumer);
    void start();
    void stop();
    void shutdown();

    interface Factory<T> {
        DataMonitor<T> create();
    }
}
