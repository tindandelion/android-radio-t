package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.live.chat.Message;
import org.dandelion.radiot.live.chat.MessageConsumer;
import org.dandelion.radiot.live.chat.ProgressListener;
import org.dandelion.radiot.live.schedule.Scheduler;

import java.util.List;

public class HttpTranslationEngine implements HttpChatRequest.ErrorListener, MessageConsumer, Scheduler.Performer {
    private final HttpChatClient chatClient;
    private final MessageConsumer consumer;
    private final ProgressListener progressListener;
    private final Scheduler pollScheduler;
    public HttpTranslationState currentState;
    public boolean isStopped = false;

    public HttpTranslationEngine(HttpChatClient chatClient, MessageConsumer consumer, ProgressListener progressListener, Scheduler pollScheduler) {
        this.chatClient = chatClient;
        this.consumer = consumer;
        this.progressListener = progressListener;
        this.pollScheduler = pollScheduler;
        pollScheduler.setPerformer(this);
        this.currentState = new HttpTranslationState.Disconnected(this);
    }

    public void connectToChat() {
        progressListener.onConnecting();
        currentState = new HttpTranslationState.Connecting(this, progressListener);
        requestLastMessages();
    }

    public void startListening() {
        currentState = new HttpTranslationState.Listening(this);
        pollScheduler.scheduleNext();
    }

    public void stopListening() {
        currentState = new HttpTranslationState.Paused(this);
    }

    public void disconnect() {
        currentState = new HttpTranslationState.Disconnected(this);
    }

    public HttpTranslationState currentState() {
        return currentState;
    }

    public void cancelPoll() {
        pollScheduler.cancel();
    }

    public void requestNextMessages() {
        requestMessages("next");
    }

    public void requestLastMessages() {
        requestMessages("last");
    }

    private void requestMessages(String mode) {
        new HttpChatRequest(mode, chatClient, this, this).execute();
    }

    @Override
    public void onError() {
        progressListener.onError();
        currentState.onError();
    }

    public void continueListening() {
        if (isStopped) {
            stopListening();
        } else {
            startListening();
        }
    }

    @Override
    public void processMessages(List<Message> messages) {
        currentState.onRequestCompleted();
        consumer.processMessages(messages);
        continueListening();
    }

    @Override
    public void performAction() {
        requestNextMessages();
    }
}
