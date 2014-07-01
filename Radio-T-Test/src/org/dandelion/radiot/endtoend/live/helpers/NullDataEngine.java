package org.dandelion.radiot.endtoend.live.helpers;

import org.dandelion.radiot.http.Consumer;
import org.dandelion.radiot.http.DataEngine;
import org.dandelion.radiot.http.ProgressListener;

public class NullDataEngine<T> implements DataEngine<T>, DataEngine.Factory {
    @Override
    public void setProgressListener(ProgressListener listener) {
    }

    @Override
    public void setDataConsumer(Consumer<T> consumer) {

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
    public DataEngine create() {
        return this;
    }
}
