package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.live.chat.Message;
import org.dandelion.radiot.live.chat.MessageConsumer;
import org.dandelion.radiot.live.chat.ProgressListener;

import java.util.List;

public class HttpTranslationState {
    protected final HttpChatTranslation translation;
    protected final ProgressListener progressListener;
    protected final HttpChatClient chatClient;
    protected final MessageConsumer consumer;


    private HttpTranslationState(HttpChatTranslation translation) {
        this(translation, translation.progressAnnouncer.announce(), translation.chatClient, null);
    }

    public HttpTranslationState(HttpChatTranslation translation, ProgressListener progressListener, HttpChatClient chatClient, MessageConsumer consumer) {
        this.translation = translation;
        this.progressListener = progressListener;
        this.chatClient = chatClient;
        this.consumer = consumer;
    }

    public void onStart() {
    }

    public void onStop() {
    }

    public void enter() {

    }

    static class Disconnected extends HttpTranslationState {

        public Disconnected(HttpChatTranslation translation, MessageConsumer consumer, HttpChatClient chatClient, ProgressListener progressListener) {
            super(translation, progressListener, chatClient, consumer);
        }

        @Override
        public void onStart() {
            Connecting newState = new Connecting(translation, chatClient, consumer, progressListener);
            translation.changeState(newState);
        }
    }

    static class Connecting extends HttpTranslationState implements MessageConsumer {
        public Connecting(HttpChatTranslation translation, HttpChatClient chatClient, MessageConsumer consumer, ProgressListener progressListener) {
            super(translation, progressListener, chatClient, consumer);
        }

        @Override
        public void onStart() {
            this.translation.progressAnnouncer.announce().onConnecting();
        }

        @Override
        public void enter() {
            progressListener.onConnecting();
            requestLastMessages();
        }

        private void requestLastMessages() {
            new HttpChatRequest.Last(chatClient, progressListener, this).execute();
        }

        @Override
        public void processMessages(List<Message> messages) {
            progressListener.onConnected();
            consumer.processMessages(messages);
        }
    }

    static class Connected extends HttpTranslationState {
        public Connected(HttpChatTranslation translation) {
            super(translation);
        }

        @Override
        public void onStart() {
            this.translation.scheduleRefresh();
        }

        @Override
        public void onStop() {
            this.translation.cancelRefresh();
        }
    }
}
