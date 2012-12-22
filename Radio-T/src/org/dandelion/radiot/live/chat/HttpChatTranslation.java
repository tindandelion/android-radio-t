package org.dandelion.radiot.live.chat;

import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

public class HttpChatTranslation implements ChatTranslation {
    private String baseUrl;
    private final DefaultHttpClient httpClient;

    public HttpChatTranslation(String baseUrl) {
        this.baseUrl = baseUrl;
        this.httpClient = new DefaultHttpClient();
    }

    @Override
    public void requestLastRecords(MessageConsumer consumer) {
        new LastRecordsRequest(chatStreamUrl("last"), consumer, httpClient).execute();
    }

    @Override
    public void requestNextRecords(MessageConsumer consumer) {
        new NextRecordsRequest(chatStreamUrl("next"), consumer, httpClient).execute();
    }

    private String chatStreamUrl(String mode) {
        return baseUrl + "/data/jsonp?mode=" + mode + "&recs=10";
    }

    private static class ConnectTask extends AsyncTask<Void, Void, List<ChatMessage>> {
        private final String url;
        protected final MessageConsumer consumer;
        private DefaultHttpClient httpClient;

        public ConnectTask(String url, MessageConsumer consumer, DefaultHttpClient httpClient) {
            this.url = url;
            this.consumer = consumer;
            this.httpClient = httpClient;
        }

        @Override
        protected List<ChatMessage> doInBackground(Void... params) {
            return parseMessages(requestMessages());
        }

        private List<ChatMessage> parseMessages(String json) {
            return ResponseParser.parse(json);
        }

        private String requestMessages() {
            try {
                Log.d("CHAT", "Connecting to chat...");
                HttpResponse response = httpClient.execute(new HttpGet(url));
                return EntityUtils.toString(response.getEntity());
            } catch (IOException e) {
                Log.d("CHAT", "Exception getting chat", e);
                throw new RuntimeException(e);
            }
        }
    }

    private static class LastRecordsRequest extends ConnectTask {
        public LastRecordsRequest(String url, MessageConsumer consumer, DefaultHttpClient httpClient) {
            super(url, consumer, httpClient);
        }

        @Override
        protected void onPostExecute(List<ChatMessage> messages) {
            consumer.initWithMessages(messages);
        }
    }

    private static class NextRecordsRequest extends ConnectTask {
        public NextRecordsRequest(String url, MessageConsumer consumer, DefaultHttpClient httpClient) {
            super(url, consumer, httpClient);
        }

        @Override
        protected void onPostExecute(List<ChatMessage> messages) {
            consumer.appendMessages(messages);
        }
    }
}
