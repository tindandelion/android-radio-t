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

    static class Paused extends HttpTranslationState {
        public Paused(StateHolder stateHolder, StateFactory stateFactory) {
            super(stateHolder, stateFactory);
        }

        @Override
        public void onStart() {
            stateHolder.changeState(stateFactory.listening(stateHolder));
        }
    }

    static class Connecting extends HttpTranslationState implements MessageConsumer, HttpChatRequest.ErrorListener {
        private final MessageConsumer consumer;
        private final ProgressListener progressListener;
        private final HttpChatClient chatClient;
        private boolean isStopped = false;

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
        public void onStop() {
            isStopped = true;
        }

        @Override
        public void enter() {
            progressListener.onConnecting();
            requestMessages();
        }

        private void requestMessages() {
            new HttpChatRequest("last", chatClient, this, this).execute();
        }

        @Override
        public void processMessages(List<Message> messages) {
            changeToState(newState());
            progressListener.onConnected();
            consumer.processMessages(messages);
        }

        private HttpTranslationState newState() {
            if (isStopped) {
                return stateFactory.paused(stateHolder);
            } else {
                return stateFactory.listening(stateHolder);
            }
        }

        @Override
        public void onError() {
            progressListener.onError();
            changeToState(stateFactory.disconnected(stateHolder));
        }
    }


    static class Listening extends HttpTranslationState implements Scheduler.Performer, MessageConsumer, HttpChatRequest.ErrorListener {
        private final MessageConsumer consumer;
        private final ProgressListener progressListener;
        private final HttpChatClient chatClient;
        private final Scheduler scheduler;
        private boolean inProgress = false;
        private boolean isStopped = false;

        Listening(StateHolder stateHolder, StateFactory stateFactory, HttpChatClient chatClient, MessageConsumer consumer, ProgressListener progressListener, Scheduler scheduler) {
            super(stateHolder, stateFactory);
            this.chatClient = chatClient;
            this.progressListener = progressListener;
            this.consumer = consumer;
            this.scheduler = scheduler;
        }

        @Override
        public void onStart() {
            requestMessages();
        }

        private void scheduleUpdate() {
            scheduler.setPerformer(this);
            scheduler.scheduleNext();
        }

        @Override
        public void enter() {
            scheduleUpdate();
        }

        @Override
        public void onStop() {
            isStopped = true;
            scheduler.cancel();
        }

        @Override
        public void performAction() {
            requestMessages();
        }

        public void requestMessages() {
            if (inProgress) return;

            inProgress = true;
            new HttpChatRequest("next", chatClient, this, this).execute();
        }

        @Override
        public void processMessages(List<Message> messages) {
            consumer.processMessages(messages);
            if (isStopped) {
                stateHolder.changeState(stateFactory.paused(stateHolder));
            } else {
                scheduleUpdate();
            }
            inProgress = false;
        }

        @Override
        public void onError() {
            progressListener.onError();
            inProgress = false;
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

        public HttpTranslationState listening(StateHolder stateHolder) {
            return new Listening(stateHolder, this, chatClient, consumer, progressListener, scheduler);
        }

        public HttpTranslationState paused(StateHolder stateHolder) {
            return new Paused(stateHolder, this);
        }
    }

}
