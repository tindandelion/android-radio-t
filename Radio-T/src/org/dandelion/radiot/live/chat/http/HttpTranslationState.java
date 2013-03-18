package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.live.chat.ProgressListener;
import org.dandelion.radiot.live.schedule.Scheduler;
import org.dandelion.radiot.util.ProgrammerError;

public class HttpTranslationState {
    protected final HttpTranslationEngine engine;

    public HttpTranslationState(HttpTranslationEngine engine) {
        this.engine = engine;
    }

    public void onStart() {
    }

    public void onStop() {
    }

    public void enter() {

    }

    public void onError() {

    }

    public void onRequestCompleted() {
        throw new ProgrammerError("Should never be called");
    }

    static class Disconnected extends HttpTranslationState {
        public Disconnected(HttpTranslationEngine httpTranslationEngine) {
            super(httpTranslationEngine);
        }

        @Override
        public void onStart() {
            engine.connectToChat();
        }
    }

    public static class Paused extends HttpTranslationState {
        public Paused(HttpTranslationEngine engine) {
            super(engine);
        }

        @Override
        public void onStart() {
            engine.startListening();
        }
    }

    static class Connecting extends HttpTranslationState  {
        private final ProgressListener progressListener;

        public Connecting(HttpTranslationEngine engine, ProgressListener progressListener) {
            super(engine);
            this.progressListener = progressListener;
        }

        @Override
        public void onStart() {
            engine.isStopped = false;
            progressListener.onConnecting();
        }

        @Override
        public void onStop() {
            engine.isStopped = true;
        }

        @Override
        public void onRequestCompleted() {
            progressListener.onConnected();
        }

        @Override
        public void onError() {
            engine.disconnect();
        }
    }


    public static class Listening extends HttpTranslationState implements Scheduler.Performer {

        public Listening(HttpTranslationEngine engine) {
            super(engine);
        }

        // TODO: This method should never be called at all
        @Override
        public void onStart() {
            engine.requestNextMessages(this);
        }

        @Override
        public void enter() {
            engine.schedulePoll(this);
        }

        @Override
        public void onStop() {
            engine.isStopped = true;
            engine.stopListening();
            engine.cancelPoll();
        }

        @Override
        public void performAction() {
            engine.requestNextMessages(this);
        }

        @Override
        public void onRequestCompleted() {

        }
    }
}
