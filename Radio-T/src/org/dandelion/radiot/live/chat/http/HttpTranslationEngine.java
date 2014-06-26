package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.common.ui.Announcer;
import org.dandelion.radiot.http.Consumer;
import org.dandelion.radiot.http.HttpRequest;
import org.dandelion.radiot.http.Provider;
import org.dandelion.radiot.live.chat.ChatTranslation;
import org.dandelion.radiot.live.chat.Message;
import org.dandelion.radiot.live.chat.MessageConsumer;
import org.dandelion.radiot.live.chat.ProgressListener;
import org.dandelion.radiot.live.schedule.Scheduler;

import java.util.List;

public class HttpTranslationEngine implements ChatTranslation {
    private final Provider<List<Message>> dataProvider;
    private Consumer<List<Message>> dataConsumer;

    private Announcer<ProgressListener> progressAnnouncer = new Announcer<>(ProgressListener.class);
    private final Scheduler pollScheduler;
    private HttpTranslationState currentState;

    public final Consumer<Exception> onError = new Consumer<Exception>() {
        @Override
        public void accept(Exception value) {
            progressAnnouncer.announce().onError();
            currentState.onError();
        }
    };

    public final Consumer<List<Message>> onMessages = new Consumer<List<Message>>() {
        @Override
        public void accept(List<Message> value) {
            currentState.onRequestCompleted();
            if (dataConsumer != null) dataConsumer.accept(value);
        }
    };

    public final Scheduler.Performer onRefresh = new Scheduler.Performer() {
        @Override
        public void performAction() {
            requestMessages();
        }
    };

    public HttpTranslationEngine(String baseUrl, Scheduler refreshScheduler) {
        this(HttpChatClient.create(baseUrl), refreshScheduler);
    }

    public HttpTranslationEngine(HttpChatClient dataProvider, Scheduler pollScheduler) {
        this.dataProvider = dataProvider;
        this.pollScheduler = pollScheduler;
        this.currentState = new HttpTranslationState.Disconnected(this);

        pollScheduler.setPerformer(onRefresh);
    }

    @Override
    public void setProgressListener(ProgressListener listener) {
        progressAnnouncer.setTarget(listener);
    }

    @Override
    public void setMessageConsumer(MessageConsumer consumer) {
        dataConsumer = consumer;
    }

    @Override
    public void start() {
        currentState.onStart();
    }

    @Override
    public void stop() {
        currentState.onStop();
    }

    @Override
    public void shutdown() {
        setMessageConsumer(null);
        setProgressListener(null);

        dataProvider.abort();
        disconnect();
    }

    public String currentState() {
        return currentState.getClass().getSimpleName();
    }

    public void disconnect() {
        setCurrentState(new HttpTranslationState.Disconnected(this));
    }

    private void requestMessages() {
        new HttpRequest<>(dataProvider, onMessages, onError).execute();
    }

    private void setCurrentState(HttpTranslationState newState) {
        this.currentState = newState;
    }

    public void startConnecting() {
        switchToConnecting();
        requestMessages();
    }

    private void switchToConnecting() {
        progressAnnouncer.announce().onConnecting();
        setCurrentState(new HttpTranslationState.Connecting(this, progressAnnouncer.announce()));
    }

    public void pauseConnecting() {
        setCurrentState(new HttpTranslationState.PausedConnecting(this, progressAnnouncer.announce()));
    }

    public void resumeConnecting() {
        switchToConnecting();
    }

    public void startListening() {
        setCurrentState(new HttpTranslationState.Listening(this));
        scheduleNextPoll();
    }

    public void pauseListening() {
        setCurrentState(new HttpTranslationState.PausedListening(this));
        pollScheduler.cancel();
    }

    public void resumeListening() {
        setCurrentState(new HttpTranslationState.Listening(this));
        scheduleNextPoll();
    }

    private void scheduleNextPoll() {
        pollScheduler.cancel();
        pollScheduler.scheduleNext();
    }
}
