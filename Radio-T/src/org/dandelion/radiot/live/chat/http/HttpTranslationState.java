package org.dandelion.radiot.live.chat.http;

public class HttpTranslationState {
    protected final HttpChatTranslation translation;

    HttpTranslationState(HttpChatTranslation translation) {
        this.translation = translation;
    }

    public void onStart() {
    }

    public void onStop() {
    }

    static class Disconnected extends HttpTranslationState {
        public Disconnected(HttpChatTranslation translation) {
            super(translation);
        }

        @Override
        public void onStart() {
            this.translation.setCurrentState(new Connecting(this.translation));
            this.translation.progressAnnouncer.announce().onConnecting();
            this.translation.requestLastMessages();
        }
    }

    static class Connecting extends HttpTranslationState {
        public Connecting(HttpChatTranslation translation) {
            super(translation);
        }

        @Override
        public void onStart() {
            this.translation.setCurrentState(this);
            this.translation.progressAnnouncer.announce().onConnecting();
        }
    }

    static class Connected extends HttpTranslationState {
        public Connected(HttpChatTranslation translation) {
            super(translation);
        }

        @Override
        public void onStart() {
            this.translation.setCurrentState(this);
            this.translation.scheduleRefresh();
        }

        @Override
        public void onStop() {
            this.translation.cancelRefresh();
        }
    }
}
