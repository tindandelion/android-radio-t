package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.live.chat.ProgressListener;

public abstract class HttpTranslationState {
    protected final HttpTranslationEngine engine;

    public HttpTranslationState(HttpTranslationEngine engine) {
        this.engine = engine;
    }

    abstract public void onStart();
    abstract public void onStop();
    abstract public void onError();
    abstract public void onRequestCompleted();

    static class Disconnected extends HttpTranslationState {
        public Disconnected(HttpTranslationEngine httpTranslationEngine) {
            super(httpTranslationEngine);
        }

        @Override
        public void onStart() {
            engine.startConnecting();
        }

        @Override
        public void onStop() {
        }

        @Override
        public void onRequestCompleted() {
        }

        @Override
        public void onError() {
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
        }

        @Override
        public void onStop() {
            engine.pauseConnecting();
        }

        @Override
        public void onRequestCompleted() {
            progressListener.onConnected();
            engine.startListening();
        }

        @Override
        public void onError() {
            engine.disconnect();
        }
    }

    public static class PausedConnecting extends HttpTranslationState {
        private final ProgressListener progressListener;

        public PausedConnecting(HttpTranslationEngine engine, ProgressListener progressListener) {
            super(engine);
            this.progressListener = progressListener;
        }

        @Override
        public void onStart() {
            engine.resumeConnecting();
        }

        @Override
        public void onStop() {
        }

        @Override
        public void onError() {
            engine.disconnect();
        }

        @Override
        public void onRequestCompleted() {
            progressListener.onConnected();
            engine.pauseListening();
        }
    }

    public static class PausedListening extends HttpTranslationState {
        public PausedListening(HttpTranslationEngine engine) {
            super(engine);
        }

        @Override
        public void onStart() {
            engine.resumeListening();
        }

        @Override
        public void onStop() {
        }

        @Override
        public void onRequestCompleted() {
        }

        @Override
        public void onError() {
            engine.disconnect();
        }
    }


    public static class Listening extends HttpTranslationState {

        public Listening(HttpTranslationEngine engine) {
            super(engine);
        }

        @Override
        public void onStart() {
        }

        @Override
        public void onStop() {
            engine.pauseListening();
        }

        @Override
        public void onError() {
            engine.disconnect();
        }

        @Override
        public void onRequestCompleted() {
            engine.startListening();
        }
    }
}
