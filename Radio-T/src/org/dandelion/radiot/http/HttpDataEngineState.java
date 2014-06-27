package org.dandelion.radiot.http;

public abstract class HttpDataEngineState {
    protected final HttpDataEngine engine;

    public HttpDataEngineState(HttpDataEngine engine) {
        this.engine = engine;
    }

    abstract public void onStart();
    abstract public void onStop();
    abstract public void onError();
    abstract public void onRequestCompleted();

    static class Disconnected extends HttpDataEngineState {
        public Disconnected(HttpDataEngine httpDataEngine) {
            super(httpDataEngine);
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

    static class Connecting extends HttpDataEngineState {
        private final ProgressListener progressListener;

        public Connecting(HttpDataEngine engine, ProgressListener progressListener) {
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

    public static class PausedConnecting extends HttpDataEngineState {
        private final ProgressListener progressListener;

        public PausedConnecting(HttpDataEngine engine, ProgressListener progressListener) {
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

    public static class PausedListening extends HttpDataEngineState {
        public PausedListening(HttpDataEngine engine) {
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


    public static class Listening extends HttpDataEngineState {

        public Listening(HttpDataEngine engine) {
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
