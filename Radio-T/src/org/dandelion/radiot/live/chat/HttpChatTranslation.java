package org.dandelion.radiot.live.chat;

import android.os.AsyncTask;

import java.util.List;

public class HttpChatTranslation implements ChatTranslation {
    private MessageConsumer consumer;
    private final HttpChatClient chatClient;

    public HttpChatTranslation(String baseUrl) {
        this(new HttpChatClient(baseUrl));
    }

    public HttpChatTranslation(HttpChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public void start(MessageConsumer consumer, ErrorListener errorListener) {
        this.consumer = consumer;
        requestLastRecords();
    }

    private void requestLastRecords() {
        new LastRecordsRequest(chatClient, consumer).execute();
    }

    @Override
    public void refresh() {
        new NextRecordsRequest(chatClient, consumer).execute();
    }

    @Override
    public void stop() {
        // TODO: Properly close connections
    }

    private static abstract class ConnectTask extends AsyncTask<Void, Void, List<Message>> {
        protected final MessageConsumer consumer;
        private Exception error;
        private final HttpChatClient chatClient;

        public ConnectTask(HttpChatClient chatClient, MessageConsumer consumer) {
            this.consumer = consumer;
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
                consumeError(error);
            } else {
                consumeMessages(messages);
            }
        }

        protected abstract void consumeMessages(List<Message> messages);
        protected abstract String mode();

        private void consumeError(Exception e) {

        }
    }

    private static class LastRecordsRequest extends ConnectTask {
        public LastRecordsRequest(HttpChatClient chatClient, MessageConsumer consumer) {
            super(chatClient, consumer);
        }

        @Override
        protected void consumeMessages(List<Message> messages) {
            consumer.initWithMessages(messages);
        }

        @Override
        protected String mode() {
            return "last";
        }
    }

    private static class NextRecordsRequest extends ConnectTask {
        private NextRecordsRequest(HttpChatClient chatClient, MessageConsumer consumer) {
            super(chatClient, consumer);
        }

        @Override
        protected void consumeMessages(List<Message> messages) {
            consumer.appendMessages(messages);
        }

        @Override
        protected String mode() {
            return "next";
        }
    }
}
