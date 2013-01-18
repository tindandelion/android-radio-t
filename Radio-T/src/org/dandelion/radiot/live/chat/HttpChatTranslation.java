package org.dandelion.radiot.live.chat;

import android.os.AsyncTask;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

public class HttpChatTranslation implements ChatTranslation {
    private String baseUrl;
    private final DefaultHttpClient httpClient;
    private MessageConsumer consumer;

    public HttpChatTranslation(String baseUrl) {
        this.baseUrl = baseUrl;
        this.httpClient = new DefaultHttpClient();
    }

    @Override
    public void start(MessageConsumer consumer) {
        this.consumer = consumer;
        requestLastRecords();
    }

    private void requestLastRecords() {
        new LastRecordsRequest(chatStreamUrl("last"), consumer, httpClient).execute();
    }

    @Override
    public void refresh() {
        new NextRecordsRequest(chatStreamUrl("next"), consumer, httpClient).execute();
    }

    @Override
    public void stop() {
        // TODO: Properly close connections
    }

    private String chatStreamUrl(String mode) {
        return baseUrl + "/data/jsonp?mode=" + mode + "&recs=10";
    }

    private static abstract class ConnectTask extends AsyncTask<Void, Void, List<Message>> {
        private final String url;
        protected final MessageConsumer consumer;
        private DefaultHttpClient httpClient;
        private Exception error;

        public ConnectTask(String url, MessageConsumer consumer, DefaultHttpClient httpClient) {
            this.url = url;
            this.consumer = consumer;
            this.httpClient = httpClient;
        }

        @Override
        protected List<Message> doInBackground(Void... params) {
            try {
                return parseMessages(requestMessages());
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

        private void consumeError(Exception e) {

        }

        private List<Message> parseMessages(String json) {
            return ResponseParser.parse(json);
        }

        private String requestMessages() throws IOException {
            HttpResponse response = httpClient.execute(new HttpGet(url));
            return EntityUtils.toString(response.getEntity());
        }
    }

    private static class LastRecordsRequest extends ConnectTask {
        public LastRecordsRequest(String url, MessageConsumer consumer, DefaultHttpClient httpClient) {
            super(url, consumer, httpClient);
        }

        @Override
        protected void consumeMessages(List<Message> messages) {
            consumer.initWithMessages(messages);
        }
    }

    private static class NextRecordsRequest extends ConnectTask {
        public NextRecordsRequest(String url, MessageConsumer consumer, DefaultHttpClient httpClient) {
            super(url, consumer, httpClient);
        }

        @Override
        protected void consumeMessages(List<Message> messages) {
            consumer.appendMessages(messages);
        }
    }
}
