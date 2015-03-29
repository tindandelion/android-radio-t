package org.dandelion.radiot.http;

public interface DataEngine<T> {
    void setProgressListener(ProgressListener listener);
    void setDataConsumer(Consumer<T> consumer);
    void setErrorConsumer(Consumer<Exception> consumer);
    void start();
    void stop();
    void shutdown();

    interface Factory<T> {
        DataEngine<T> create();
    }
}
