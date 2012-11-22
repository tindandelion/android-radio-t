package org.dandelion.radiot.live.ui;

import android.os.AsyncTask;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

public class HttpChatTranslation implements ChatTranslation {
    private String baseUrl;

    public HttpChatTranslation(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void connect() {
        new ConnectTask(connectUrl()).execute();
    }

    private String connectUrl() {
        return baseUrl + "/data/jsonp?mode=last&recs=10";
    }

    private static class ConnectTask extends AsyncTask<Void, Void, Void> {
        private String url;

        public ConnectTask(String url) {
            this.url = url;
        }

        @Override
        protected Void doInBackground(Void... params) {
            DefaultHttpClient client = new DefaultHttpClient();
            try {
                client.execute(new HttpGet(url));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        }
    }
}
