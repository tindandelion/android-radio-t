package org.dandelion.radiot.http;

public interface DataEngine<T> {
    void setProgressListener(ProgressListener listener);
    void setDataConsumer(Consumer<T> consumer);
    void start();
    void stop();
    void shutdown();

    public interface Factory {
        DataEngine create();
    }
}
