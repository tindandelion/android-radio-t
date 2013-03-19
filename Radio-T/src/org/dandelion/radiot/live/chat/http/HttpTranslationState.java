package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.live.chat.ProgressListener;
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
            engine.connect();
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

        @Override
        public void onRequestCompleted() {
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
            progressListener.onConnecting();
        }

        @Override
        public void onStop() {
            engine.stopListening();
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


    public static class Listening extends HttpTranslationState {

        public Listening(HttpTranslationEngine engine) {
            super(engine);
        }

        @Override
        public void onStop() {
            engine.stopListening();
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
