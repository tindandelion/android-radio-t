package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.live.chat.MessageConsumer;
import org.dandelion.radiot.live.chat.ProgressListener;

public class HttpTranslationState {
    protected final HttpChatTranslation translation;

    HttpTranslationState(HttpChatTranslation translation) {
        this.translation = translation;
    }

    public void onStart() {
    }

    public void onStop() {
    }

    static class Disconnected extends HttpTranslationState {
        private final MessageConsumer messageConsumer;
        private final HttpChatClient chatClient;
        private final ProgressListener progressListener;

        public Disconnected(HttpChatTranslation translation) {
            this(translation, translation, translation.chatClient, translation.progressAnnouncer.announce());
        }

        public Disconnected(HttpChatTranslation translation, MessageConsumer consumer, HttpChatClient chatClient, ProgressListener progressListener) {
            super(translation);
            this.messageConsumer = consumer;
            this.chatClient = chatClient;
            this.progressListener = progressListener;
        }

        @Override
        public void onStart() {
            this.translation.setCurrentState(new Connecting(this.translation));
            progressListener.onConnecting();
            requestLastMessages();
        }

        private void requestLastMessages() {
            new HttpChatRequest.Last(chatClient, progressListener, messageConsumer).execute();
        }
    }

    static class Connecting extends HttpTranslationState {
        public Connecting(HttpChatTranslation translation) {
            super(translation);
        }

        @Override
        public void onStart() {
            this.translation.setCurrentState(this);
            this.translation.progressAnnouncer.announce().onConnecting();
        }
    }

    static class Connected extends HttpTranslationState {
        public Connected(HttpChatTranslation translation) {
            super(translation);
        }

        @Override
        public void onStart() {
            this.translation.setCurrentState(this);
            this.translation.scheduleRefresh();
        }

        @Override
        public void onStop() {
            this.translation.cancelRefresh();
        }
    }
}
