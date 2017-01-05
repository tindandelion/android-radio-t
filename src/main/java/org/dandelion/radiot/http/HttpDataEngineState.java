package org.dandelion.radiot.http;

public abstract class HttpDataEngineState {
    protected final HttpDataMonitor engine;

    public HttpDataEngineState(HttpDataMonitor engine) {
        this.engine = engine;
    }

    abstract public void start();

    abstract public void stop();

    abstract public void onError();

    abstract public void completeRequest();

    public <T> void invokeConsumer(Consumer<T> consumer, T value) {
        if (consumer != null) consumer.accept(value);
    }

    static class Disconnected extends HttpDataEngineState {
        public Disconnected(HttpDataMonitor httpDataEngine) {
            super(httpDataEngine);
        }

        @Override
        public void start() {
            engine.startConnecting();
        }

        @Override
        public void stop() {
        }

        @Override
        public void completeRequest() {
        }

        @Override
        public void onError() {
        }

        @Override
        public <T> void invokeConsumer(Consumer<T> consumer, T value) {
        }
    }

    static class Connecting extends HttpDataEngineState {
        private final ProgressListener progressListener;

        public Connecting(HttpDataMonitor engine, ProgressListener progressListener) {
            super(engine);
            this.progressListener = progressListener;
        }

        @Override
        public void start() {
        }

        @Override
        public void stop() {
            engine.pauseConnecting();
        }

        @Override
        public void completeRequest() {
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

        public PausedConnecting(HttpDataMonitor engine, ProgressListener progressListener) {
            super(engine);
            this.progressListener = progressListener;
        }

        @Override
        public void start() {
            engine.resumeConnecting();
        }

        @Override
        public void stop() {
        }

        @Override
        public void onError() {
            engine.disconnect();
        }

        @Override
        public void completeRequest() {
            progressListener.onConnected();
            engine.pauseListening();
        }

        @Override
        public <T> void invokeConsumer(Consumer<T> consumer, T value) {
        }
    }

    public static class PausedListening extends HttpDataEngineState {
        public PausedListening(HttpDataMonitor engine) {
            super(engine);
        }

        @Override
        public void start() {
            engine.resumeListening();
        }

        @Override
        public void stop() {
        }

        @Override
        public void completeRequest() {
        }

        @Override
        public void onError() {
            engine.disconnect();
        }

        @Override
        public <T> void invokeConsumer(Consumer<T> consumer, T value) {
        }
    }


    public static class Listening extends HttpDataEngineState {

        public Listening(HttpDataMonitor engine) {
            super(engine);
        }

        @Override
        public void start() {
        }

        @Override
        public void stop() {
            engine.pauseListening();
        }

        @Override
        public void onError() {
            engine.disconnect();
        }

        @Override
        public void completeRequest() {
            engine.startListening();
        }
    }
}
