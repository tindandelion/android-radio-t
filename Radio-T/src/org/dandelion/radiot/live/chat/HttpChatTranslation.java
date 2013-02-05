package org.dandelion.radiot.live.chat;

import android.os.AsyncTask;
import java.util.List;

public class HttpChatTranslation implements ChatTranslation {
    private MessageConsumer messageConsumer;
    private ProgressListener progressListener;
    private final HttpChatClient chatClient;
    private boolean isActive;

    public HttpChatTranslation(String baseUrl) {
        this(new HttpChatClient(baseUrl));
    }

    public HttpChatTranslation(HttpChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public void start(MessageConsumer consumer, ProgressListener listener) {
        this.messageConsumer = consumer;
        this.progressListener = listener;
        isActive = true;
        requestLastRecords();
    }

    private void requestLastRecords() {
        new LastRecordsRequest(this).execute();
    }

    @Override
    public void refresh() {
        new NextRecordsRequest(this).execute();
    }

    @Override
    public void stop() {
        isActive = false;
        chatClient.shutdown();
    }

    private static abstract class ChatTranslationTask extends AsyncTask<Void, Void, List<Message>> {
        protected final HttpChatTranslation translation;
        private Exception error;
        private String mode;

        protected ChatTranslationTask(HttpChatTranslation translation, String mode) {
            this.translation = translation;
            this.mode = mode;
        }

        @Override
        protected List<Message> doInBackground(Void... params) {
            try {
                return translation.chatClient.retrieveMessages(mode);
            } catch (Exception e) {
                error = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Message> messages) {
            if (!translation.isActive) {
                return;
            }

            if (error != null) {
                reportError();
            } else {
                reportSuccess();
                consumeMessages(messages);
            }
        }

        private void consumeMessages(List<Message> messages) {
            translation.messageConsumer.appendMessages(messages);
        }

        private void reportError() {
            translation.progressListener.onError();
        }

        protected void reportSuccess() {

        }

    }

    private static class LastRecordsRequest extends ChatTranslationTask {
        public LastRecordsRequest(HttpChatTranslation translation) {
            super(translation, "last");
        }

        @Override
        protected void onPreExecute() {
            translation.progressListener.onConnecting();
        }

        @Override
        protected void reportSuccess() {
            translation.progressListener.onConnected();
        }
    }

    private static class NextRecordsRequest extends ChatTranslationTask {
        private NextRecordsRequest(HttpChatTranslation translation) {
            super(translation, "next");
        }
    }
}
