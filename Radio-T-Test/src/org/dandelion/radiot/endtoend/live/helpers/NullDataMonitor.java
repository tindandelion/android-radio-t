package org.dandelion.radiot.endtoend.live.helpers;

import org.dandelion.radiot.http.Consumer;
import org.dandelion.radiot.http.DataMonitor;
import org.dandelion.radiot.http.ProgressListener;

public class NullDataMonitor<T> implements DataMonitor<T>, DataMonitor.Factory {
    @Override
    public void setProgressListener(ProgressListener listener) {
    }

    @Override
    public void setDataConsumer(Consumer<T> consumer) {

    }

    @Override
    public void setErrorConsumer(Consumer<Exception> consumer) {

    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public void shutdown() {
    }

    @Override
    public DataMonitor create() {
        return this;
    }
}
