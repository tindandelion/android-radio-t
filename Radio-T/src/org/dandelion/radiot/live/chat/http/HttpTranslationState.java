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


    public HttpTranslationState(HttpChatTranslation translation, HttpChatClient chatClient, MessageConsumer consumer, ProgressListener progressListener) {
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

    protected Connecting connecting() {
        return new Connecting(translation, chatClient, consumer, progressListener);
    }

    protected Connected connected() {
        return new Connected(translation, chatClient, consumer, progressListener);
    }

    static class Disconnected extends HttpTranslationState {

        public Disconnected(HttpChatTranslation translation, MessageConsumer consumer, HttpChatClient chatClient, ProgressListener progressListener) {
            super(translation, chatClient, consumer, progressListener);
        }

        @Override
        public void onStart() {
            Connecting newState = connecting();
            translation.changeState(newState);
        }

    }

    static class Connecting extends HttpTranslationState implements MessageConsumer {
        public Connecting(HttpChatTranslation translation, HttpChatClient chatClient, MessageConsumer consumer, ProgressListener progressListener) {
            super(translation, chatClient, consumer, progressListener);
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
            Connected newState = connected();
            translation.changeState(newState);
            progressListener.onConnected();
            consumer.processMessages(messages);
        }
    }


    static class Connected extends HttpTranslationState {
        public Connected(HttpChatTranslation translation) {
            super(translation, translation.chatClient, null, translation.progressAnnouncer.announce());
        }

        Connected(HttpChatTranslation translation, HttpChatClient chatClient, MessageConsumer consumer, ProgressListener progressListener) {
            super(translation, chatClient, consumer, progressListener);
        }

        @Override
        public void onStart() {
            translation.scheduleRefresh();
        }

        @Override
        public void enter() {
        }

        @Override
        public void onStop() {
            translation.cancelRefresh();
        }
    }
}
