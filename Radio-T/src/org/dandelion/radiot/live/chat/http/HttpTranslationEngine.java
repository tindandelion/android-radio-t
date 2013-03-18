package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.live.chat.Message;
import org.dandelion.radiot.live.chat.MessageConsumer;
import org.dandelion.radiot.live.chat.ProgressListener;
import org.dandelion.radiot.live.schedule.Scheduler;

import java.util.List;

public class HttpTranslationEngine implements HttpChatRequest.ErrorListener {
    private final HttpChatClient chatClient;
    private final MessageConsumer consumer;
    private final ProgressListener progressListener;
    private final Scheduler pollScheduler;
    private HttpTranslationState currentState;
    private boolean requestInProgress = false;
    public boolean isStopped = false;

    public HttpTranslationEngine(HttpChatClient chatClient, MessageConsumer consumer, ProgressListener progressListener, Scheduler pollScheduler) {
        this.chatClient = chatClient;
        this.consumer = consumer;
        this.progressListener = progressListener;
        this.pollScheduler = pollScheduler;
        this.currentState = new HttpTranslationState.Disconnected(this);
    }

    public void beConnecting() {
        HttpTranslationState.Connecting connecting = new HttpTranslationState.Connecting(this,
                progressListener);
        currentState = connecting;
        progressListener.onConnecting();
        requestLastMessages(connecting);
    }

    public void beListening() {
        HttpTranslationState.Listening listening = new HttpTranslationState.Listening(this);
        changeState(listening);
    }

    public void bePaused() {
        currentState = new HttpTranslationState.Paused(this);

    }

    public void beDisconnected() {
        currentState = new HttpTranslationState.Disconnected(this);
    }

    public HttpTranslationState currentState() {
        return currentState;
    }

    public void changeState(HttpTranslationState newState) {
        currentState = newState;
        newState.enter();
    }

    public void schedulePoll(Scheduler.Performer performer) {
        pollScheduler.setPerformer(performer);
        pollScheduler.scheduleNext();
    }

    public void cancelPoll() {
        pollScheduler.cancel();
    }

    public void requestNextMessages(final HttpTranslationState state) {
        requestMessages("next", state);
    }

    public void requestLastMessages(final HttpTranslationState state) {
        requestMessages("last", state);
    }

    private void requestMessages(String mode, final HttpTranslationState state) {
        if (requestInProgress) return;
        requestInProgress = true;
        MessageConsumer consumerWrapper = new MessageConsumer() {
            @Override
            public void processMessages(List<Message> messages) {
                continueListening();
                state.onRequestCompleted();
                consumer.processMessages(messages);
                requestInProgress = false;
            }
        };

        new HttpChatRequest(mode, chatClient, consumerWrapper, this).execute();
    }

    @Override
    public void onError() {
        progressListener.onError();
        currentState.onError();
    }

    public void continueListening() {
        if (isStopped) {
            bePaused();
        } else {
            beListening();
        }
    }
}
