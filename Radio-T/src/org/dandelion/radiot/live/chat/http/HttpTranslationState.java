package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.live.chat.ProgressListener;
import org.dandelion.radiot.live.schedule.Scheduler;

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

    }

    static class Disconnected extends HttpTranslationState {
        public Disconnected(HttpTranslationEngine httpTranslationEngine) {
            super(httpTranslationEngine);
        }

        @Override
        public void onStart() {
            engine.beConnecting();
        }
    }

    public static class Paused extends HttpTranslationState {
        public Paused(HttpTranslationEngine engine) {
            super(engine);
        }

        @Override
        public void onStart() {
            engine.beListening();
        }
    }

    static class Connecting extends HttpTranslationState  {
        private final ProgressListener progressListener;
        private boolean isStopped = false;

        public Connecting(HttpTranslationEngine engine, ProgressListener progressListener) {
            super(engine);
            this.progressListener = progressListener;
        }

        @Override
        public void onStart() {
            isStopped = false;
            progressListener.onConnecting();
        }

        @Override
        public void onStop() {
            isStopped = true;
        }

        @Override
        public void enter() {
            progressListener.onConnecting();
            engine.requestLastMessages(this);
        }

        @Override
        public void onRequestCompleted() {
            if (isStopped) {
                engine.bePaused();
            } else {
                engine.beListening();
            }

            progressListener.onConnected();
        }

        @Override
        public void onError() {
            engine.beDisconnected();
        }
    }


    public static class Listening extends HttpTranslationState implements Scheduler.Performer {
        private boolean isStopped = false;

        public Listening(HttpTranslationEngine engine) {
            super(engine);
        }

        // TODO: This method should never be called at all
        @Override
        public void onStart() {
            engine.requestNextMessages(this);
        }

        private void scheduleUpdate() {
            engine.schedulePoll(this);
        }

        @Override
        public void enter() {
            scheduleUpdate();
        }

        @Override
        public void onStop() {
            isStopped = true;
            engine.cancelPoll();
            engine.bePaused();
        }

        @Override
        public void performAction() {
            engine.requestNextMessages(this);
        }

        @Override
        public void onRequestCompleted() {
            if (isStopped) {
                engine.bePaused();
            } else {
                engine.schedulePoll(this);
            }
        }
    }
}
