package org.dandelion.radiot.endtoend.live;

import org.dandelion.radiot.live.chat.ChatTranslation;
import org.dandelion.radiot.live.chat.MessageConsumer;
import org.dandelion.radiot.live.chat.ProgressListener;

public class NullChatTranslation implements ChatTranslation, ChatTranslation.Factory {
    @Override
    public void setProgressListener(ProgressListener listener) {
    }

    @Override
    public void setMessageConsumer(MessageConsumer consumer) {
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
    public ChatTranslation create() {
        return this;
    }
}
