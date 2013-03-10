package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.live.chat.MessageConsumer;

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

        public Disconnected(HttpChatTranslation translation) {
            this(translation, translation, translation.chatClient);
        }

        public Disconnected(HttpChatTranslation translation, MessageConsumer consumer, HttpChatClient chatClient) {
            super(translation);
            this.messageConsumer = consumer;
            this.chatClient = chatClient;
        }

        @Override
        public void onStart() {
            this.translation.setCurrentState(new Connecting(this.translation));
            this.translation.progressAnnouncer.announce().onConnecting();
            this.translation.requestLastMessages(messageConsumer, chatClient);
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
