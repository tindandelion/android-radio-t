package org.dandelion.radiot.http;

import org.dandelion.radiot.common.Announcer;
import org.dandelion.radiot.common.Scheduler;

public class HttpDataEngine<T> implements DataEngine<T> {
    private final Provider<T> dataProvider;
    private Consumer<T> dataConsumer;
    private Consumer<Exception> errorConsumer;

    private Announcer<ProgressListener> progressAnnouncer = new Announcer<>(ProgressListener.class);
    private final Scheduler pollScheduler;
    private HttpDataEngineState currentState;

    public final Consumer<Exception> onError = new Consumer<Exception>() {
        @Override
        public void accept(Exception error) {
            progressAnnouncer.announce().onError();
            if (errorConsumer != null) errorConsumer.accept(error);
            currentState.onError();
        }
    };

    public final Consumer<T> onMessages = new Consumer<T>() {
        @Override
        public void accept(T value) {
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

    public HttpDataEngine(Provider<T> dataProvider, Scheduler pollScheduler) {
        this.dataProvider = dataProvider;
        this.pollScheduler = pollScheduler;
        this.currentState = new HttpDataEngineState.Disconnected(this);

        pollScheduler.setPerformer(onRefresh);
    }

    @Override
    public void setProgressListener(ProgressListener listener) {
        progressAnnouncer.setTarget(listener);
    }

    @Override
    public void setDataConsumer(Consumer<T> consumer) {
        dataConsumer = consumer;
    }

    @Override
    public void setErrorConsumer(Consumer<Exception> consumer) {
        this.errorConsumer = consumer;
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
        setDataConsumer(null);
        setProgressListener(null);
        setErrorConsumer(null);

        dataProvider.abort();
        disconnect();
    }

    public String currentState() {
        return currentState.getClass().getSimpleName();
    }

    public void disconnect() {
        setCurrentState(new HttpDataEngineState.Disconnected(this));
    }

    private void requestMessages() {
        new HttpRequest<>(dataProvider, onMessages, onError).execute();
    }

    private void setCurrentState(HttpDataEngineState newState) {
        this.currentState = newState;
    }

    public void startConnecting() {
        switchToConnecting();
        requestMessages();
    }

    private void switchToConnecting() {
        progressAnnouncer.announce().onConnecting();
        setCurrentState(new HttpDataEngineState.Connecting(this, progressAnnouncer.announce()));
    }

    public void pauseConnecting() {
        setCurrentState(new HttpDataEngineState.PausedConnecting(this, progressAnnouncer.announce()));
    }

    public void resumeConnecting() {
        switchToConnecting();
    }

    public void startListening() {
        setCurrentState(new HttpDataEngineState.Listening(this));
        scheduleNextPoll();
    }

    public void pauseListening() {
        setCurrentState(new HttpDataEngineState.PausedListening(this));
        pollScheduler.cancel();
    }

    public void resumeListening() {
        setCurrentState(new HttpDataEngineState.Listening(this));
        scheduleNextPoll();
    }

    private void scheduleNextPoll() {
        pollScheduler.cancel();
        pollScheduler.scheduleNext();
    }
}
