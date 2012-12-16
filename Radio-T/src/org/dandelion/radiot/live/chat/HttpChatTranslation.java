package org.dandelion.radiot.live.chat;

import android.os.AsyncTask;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
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

    private static class ResponseParser {
        private final InputStream stream;

        public static List<String> parse(InputStream content) {
            return new ResponseParser(content).parse();
        }

        private ResponseParser(InputStream stream) {
            this.stream = stream;
        }

        private List<String> parse() {
            return Arrays.asList("Lorem ipsum");
        }
    }

    private static class ConnectTask extends AsyncTask<Void, Void, List<String>> {
        private final String url;
        private final MessageConsumer consumer;

        public ConnectTask(String url, MessageConsumer consumer) {
            this.url = url;
            this.consumer = consumer;
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            return parseMessages(requestMessages());
        }

        private List<String> parseMessages(HttpResponse response) {
            try {
                return ResponseParser.parse(response.getEntity().getContent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private HttpResponse requestMessages() {
            DefaultHttpClient client = new DefaultHttpClient();
            try {
                return client.execute(new HttpGet(url));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(List<String> messages) {
            consumer.addMessages(messages);
        }
    }
}
