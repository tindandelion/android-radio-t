package org.dandelion.radiot.live.chat;

import android.os.AsyncTask;

import java.util.List;

public class HttpChatTranslation implements ChatTranslation {
    private MessageConsumer messageConsumer;
    private ProgressListener progressListener;
    private final HttpChatClient chatClient;

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
        requestLastRecords();
    }

    private void requestLastRecords() {
        new LastRecordsRequest(chatClient, messageConsumer, progressListener).execute();
    }

    @Override
    public void refresh() {
        new NextRecordsRequest(chatClient, messageConsumer, progressListener).execute();
    }

    @Override
    public void stop() {
        chatClient.shutdown();
    }

    private static abstract class ChatTranslationTask extends AsyncTask<Void, Void, List<Message>> {
        protected final MessageConsumer consumer;
        protected final ProgressListener listener;
        private Exception error;
        private final HttpChatClient chatClient;

        public ChatTranslationTask(HttpChatClient chatClient, MessageConsumer consumer, ProgressListener listener) {
            this.consumer = consumer;
            this.listener = listener;
            this.chatClient = chatClient;
        }

        @Override
        protected List<Message> doInBackground(Void... params) {
            try {
                return chatClient.retrieveMessages(mode());
            } catch (Exception e) {
                error = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Message> messages) {
            if (error != null) {
                reportError();
            } else {
                reportSuccess();
                consumer.appendMessages(messages);
            }
        }

        private void reportError() {
            listener.onError();
        }

        protected void reportSuccess() {

        }

        protected abstract String mode();
    }

    private static class LastRecordsRequest extends ChatTranslationTask {
        public LastRecordsRequest(HttpChatClient chatClient, MessageConsumer consumer, ProgressListener listener) {
            super(chatClient, consumer, listener);
        }

        @Override
        protected void onPreExecute() {
            listener.onConnecting();
        }

        @Override
        protected void reportSuccess() {
            listener.onConnected();
        }

        @Override
        protected String mode() {
            return "last";
        }
    }

    private static class NextRecordsRequest extends ChatTranslationTask {
        private NextRecordsRequest(HttpChatClient chatClient, MessageConsumer consumer, ProgressListener listener) {
            super(chatClient, consumer, listener);
        }

        @Override
        protected String mode() {
            return "next";
        }
    }
}
