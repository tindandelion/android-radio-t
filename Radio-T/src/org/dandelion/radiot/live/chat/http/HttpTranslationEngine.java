package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.live.chat.MessageConsumer;
import org.dandelion.radiot.live.chat.ProgressListener;
import org.dandelion.radiot.live.schedule.Scheduler;

public class HttpTranslationEngine {
    private final HttpTranslationState.StateHolder stateHolder;
    private final HttpChatClient chatClient;
    private final MessageConsumer consumer;
    private final ProgressListener progressListener;
    private final Scheduler pollScheduler;

    public HttpTranslationEngine(HttpTranslationState.StateHolder stateHolder, HttpChatClient chatClient, MessageConsumer consumer, ProgressListener progressListener, Scheduler pollScheduler) {
        this.chatClient = chatClient;
        this.consumer = consumer;
        this.progressListener = progressListener;
        this.pollScheduler = pollScheduler;
        this.stateHolder = stateHolder;
    }

    public void beConnecting() {
        HttpTranslationState.Connecting connecting = new HttpTranslationState.Connecting(this,
                        chatClient, consumer, progressListener);
        stateHolder.changeState(connecting);
    }

    public void beListening() {
        HttpTranslationState.Listening listening = new HttpTranslationState.Listening(this, chatClient, consumer, progressListener, pollScheduler);
        stateHolder.changeState(listening);
    }

    public void bePaused() {
        HttpTranslationState.Paused paused =
                new HttpTranslationState.Paused(this);
        stateHolder.changeState(paused);
    }

    public void beDisconnected() {
        HttpTranslationState disconnected =
                new HttpTranslationState.Disconnected(this);
        stateHolder.changeState(disconnected);
    }
}
