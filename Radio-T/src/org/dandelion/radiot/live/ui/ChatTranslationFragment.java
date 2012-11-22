package org.dandelion.radiot.live.ui;

import android.os.AsyncTask;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.dandelion.radiot.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

public class ChatTranslationFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chat_translation, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        connectToServer();
    }

    @SuppressWarnings("unchecked")
    private void connectToServer() {
        new ConnectTask().execute();
    }

    private static class ConnectTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            final String url = "http://127.0.0.1:32768/data/jsonp?mode=last&recs=10";
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
