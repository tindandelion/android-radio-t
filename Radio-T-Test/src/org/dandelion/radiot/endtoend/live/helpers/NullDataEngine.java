package org.dandelion.radiot.endtoend.live.helpers;

import org.dandelion.radiot.http.Consumer;
import org.dandelion.radiot.http.DataEngine;
import org.dandelion.radiot.live.chat.Message;
import org.dandelion.radiot.http.ProgressListener;

import java.util.List;

public class NullDataEngine implements DataEngine<List<Message>>, DataEngine.Factory {
    @Override
    public void setProgressListener(ProgressListener listener) {
    }

    @Override
    public void setDataConsumer(Consumer<List<Message>> consumer) {

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
