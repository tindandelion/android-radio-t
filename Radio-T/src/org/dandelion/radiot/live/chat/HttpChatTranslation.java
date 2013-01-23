package org.dandelion.radiot.live.chat;

import android.os.AsyncTask;

import java.util.List;

public class HttpChatTranslation implements ChatTranslation {
    private MessageConsumer messageConsumer;
    private ErrorListener errorListener;
    private final HttpChatClient chatClient;

    public HttpChatTranslation(String baseUrl) {
        this(new HttpChatClient(baseUrl));
    }

    public HttpChatTranslation(HttpChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public void start(MessageConsumer consumer, ErrorListener errorListener) {
        this.messageConsumer = consumer;
        this.errorListener = errorListener;
        requestLastRecords();
    }

    private void requestLastRecords() {
        new LastRecordsRequest(chatClient, messageConsumer, errorListener).execute();
    }

    @Override
    public void refresh() {
        new NextRecordsRequest(chatClient, messageConsumer, errorListener).execute();
    }

    @Override
    public void stop() {
        chatClient.shutdown();
    }

    private static abstract class ChatTranslationTask extends AsyncTask<Void, Void, List<Message>> {
        protected final MessageConsumer consumer;
        private Exception error;
        private final HttpChatClient chatClient;
        private final ErrorListener errorListener;

        public ChatTranslationTask(HttpChatClient chatClient, MessageConsumer consumer, ErrorListener errorListener) {
            this.consumer = consumer;
            this.errorListener = errorListener;
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
                consumeError();
            } else {
                consumeMessages(messages);
            }
        }

        protected abstract void consumeMessages(List<Message> messages);
        protected abstract String mode();

        private void consumeError() {
            errorListener.onError();
        }
    }

    private static class LastRecordsRequest extends ChatTranslationTask {
        public LastRecordsRequest(HttpChatClient chatClient, MessageConsumer consumer, ErrorListener errorListener) {
            super(chatClient, consumer, errorListener);
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

    private static class NextRecordsRequest extends ChatTranslationTask {
        private NextRecordsRequest(HttpChatClient chatClient, MessageConsumer consumer, ErrorListener errorListener) {
            super(chatClient, consumer, errorListener);
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
