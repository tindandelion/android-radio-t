package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.live.chat.Message;
import org.dandelion.radiot.live.chat.MessageConsumer;
import org.dandelion.radiot.live.chat.ProgressListener;
import org.dandelion.radiot.live.schedule.Scheduler;

import java.util.List;

public class HttpTranslationState {
    protected final StateFactory stateFactory;
    protected final StateHolder stateHolder;

    public HttpTranslationState(StateHolder stateHolder, StateFactory stateFactory) {
        this.stateFactory = stateFactory;
        this.stateHolder = stateHolder;
    }

    public void onStart() {
    }

    public void onStop() {
    }

    public void enter() {

    }

    protected void changeToState(HttpTranslationState newState) {
        stateHolder.changeState(newState);
    }

    static class Disconnected extends HttpTranslationState {
        public Disconnected(StateHolder stateHolder, StateFactory stateFactory) {
            super(stateHolder, stateFactory);
        }

        @Override
        public void onStart() {
            changeToState(stateFactory.connecting(stateHolder));
        }

    }

    static class Connecting extends HttpTranslationState implements MessageConsumer {
        private final MessageConsumer consumer;
        private final ProgressListener progressListener;
        private final HttpChatClient chatClient;

        public Connecting(StateHolder stateHolder, StateFactory stateFactory, HttpChatClient chatClient, MessageConsumer consumer, ProgressListener progressListener) {
            super(stateHolder, stateFactory);
            this.chatClient = chatClient;
            this.progressListener = progressListener;
            this.consumer = consumer;
        }

        @Override
        public void onStart() {
            progressListener.onConnecting();
        }

        @Override
        public void enter() {
            progressListener.onConnecting();
            requestMessages();
        }

        private void requestMessages() {
            new HttpChatRequest("last", chatClient, progressListener, this).execute();
        }

        @Override
        public void processMessages(List<Message> messages) {
            changeToState(stateFactory.connected(stateHolder));
            progressListener.onConnected();
            consumer.processMessages(messages);
        }
    }


    static class Connected extends HttpTranslationState implements Scheduler.Performer, MessageConsumer {
        private final MessageConsumer consumer;
        private final ProgressListener progressListener;
        private final HttpChatClient chatClient;
        private final Scheduler scheduler;

        Connected(StateHolder stateHolder, StateFactory stateFactory, HttpChatClient chatClient, MessageConsumer consumer, ProgressListener progressListener, Scheduler scheduler) {
            super(stateHolder, stateFactory);
            this.chatClient = chatClient;
            this.progressListener = progressListener;
            this.consumer = consumer;
            this.scheduler = scheduler;
        }

        @Override
        public void onStart() {
            scheduleNext();
        }

        private void scheduleNext() {
            scheduler.setPerformer(this);
            scheduler.scheduleNext();
        }

        @Override
        public void enter() {
            scheduleNext();
        }

        @Override
        public void onStop() {
            scheduler.cancel();
        }

        @Override
        public void performAction() {
            new HttpChatRequest("next", chatClient, progressListener, this).execute();
        }

        @Override
        public void processMessages(List<Message> messages) {
            consumer.processMessages(messages);
            scheduleNext();
        }
    }

    public interface StateHolder {
        void changeState(HttpTranslationState newState);
    }

    public static class StateFactory {
        private final HttpChatClient chatClient;
        private final MessageConsumer consumer;
        private final ProgressListener progressListener;
        private final Scheduler scheduler;

        public StateFactory(HttpChatClient chatClient, MessageConsumer consumer, ProgressListener progressListener, Scheduler scheduler) {
            this.chatClient = chatClient;
            this.consumer = consumer;
            this.progressListener = progressListener;
            this.scheduler = scheduler;
        }

        public HttpTranslationState disconnected(StateHolder stateHolder) {
            return new Disconnected(stateHolder, this);
        }

        public HttpTranslationState connecting(StateHolder stateHolder) {
            return new Connecting(stateHolder, this, chatClient, consumer, progressListener);
        }

        public HttpTranslationState connected(StateHolder stateHolder) {
            return new Connected(stateHolder, this, chatClient, consumer, progressListener, scheduler);
        }
    }
}
