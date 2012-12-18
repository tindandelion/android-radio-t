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

    public HttpChatTranslation(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public void requestLastRecords(MessageConsumer consumer) {
        new ConnectTask(connectUrl(), consumer).execute();
    }

    private String connectUrl() {
        return baseUrl + "/data/jsonp?mode=last&recs=10";
    }

    private static class ConnectTask extends AsyncTask<Void, Void, List<ChatMessage>> {
        private final String url;
        private final MessageConsumer consumer;

        public ConnectTask(String url, MessageConsumer consumer) {
            this.url = url;
            this.consumer = consumer;
        }

        @Override
        protected List<ChatMessage> doInBackground(Void... params) {
            return parseMessages(requestMessages());
        }

        private List<ChatMessage> parseMessages(String json) {
            return ResponseParser.parse(json);
        }

        private String requestMessages() {
            DefaultHttpClient client = new DefaultHttpClient();
            try {
                Log.d("CHAT", "Connecting to chat...");
                HttpResponse response = client.execute(new HttpGet(url));
                return EntityUtils.toString(response.getEntity());
            } catch (IOException e) {
                Log.d("CHAT", "Exception getting chat", e);
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(List<ChatMessage> messages) {
            consumer.addMessages(messages);
        }
    }
}
