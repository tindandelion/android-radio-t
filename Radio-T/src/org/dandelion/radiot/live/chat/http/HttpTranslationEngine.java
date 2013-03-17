package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.live.chat.MessageConsumer;
import org.dandelion.radiot.live.chat.ProgressListener;
import org.dandelion.radiot.live.schedule.Scheduler;

public class HttpTranslationEngine implements HttpTranslationState.StateHolder {
    private final HttpTranslationState.StateHolder stateHolder;
    private final HttpChatClient chatClient;
    private final MessageConsumer consumer;
    private final ProgressListener progressListener;
    private final Scheduler pollScheduler;
    private HttpTranslationState currentState;

    public HttpTranslationEngine(HttpTranslationState.StateHolder stateHolder, HttpChatClient chatClient, MessageConsumer consumer, ProgressListener progressListener, Scheduler pollScheduler) {
        this.chatClient = chatClient;
        this.consumer = consumer;
        this.progressListener = progressListener;
        this.pollScheduler = pollScheduler;
        this.stateHolder = stateHolder;
        this.currentState = new HttpTranslationState.Disconnected(this);
    }

    public void beConnecting() {
        HttpTranslationState.Connecting connecting = new HttpTranslationState.Connecting(this,
                        chatClient, consumer, progressListener);
        changeState(connecting);
    }

    public void beListening() {
        HttpTranslationState.Listening listening = new HttpTranslationState.Listening(this, chatClient, consumer, progressListener, pollScheduler);
        changeState(listening);
    }

    public void bePaused() {
        HttpTranslationState.Paused paused =
                new HttpTranslationState.Paused(this);
        changeState(paused);
    }

    public void beDisconnected() {
        HttpTranslationState disconnected =
                new HttpTranslationState.Disconnected(this);
        changeState(disconnected);
    }

    public HttpTranslationState currentState() {
        return currentState;
    }

    @Override
    public void changeState(HttpTranslationState newState) {
        currentState = newState;
        stateHolder.changeState(newState);
    }
}
