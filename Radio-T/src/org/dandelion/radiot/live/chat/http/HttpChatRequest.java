package org.dandelion.radiot.live.chat.http;

import android.os.AsyncTask;
import org.dandelion.radiot.http.Consumer;

@SuppressWarnings("unchecked")
public class HttpChatRequest<T> extends AsyncTask<Void, Void, Object> {

    public interface ErrorListener {
        void onError();
    }

    private final HttpChatClient chatClient;
    private final ErrorListener errorListener;
    private final Consumer<T> consumer;

    protected HttpChatRequest(HttpChatClient chatClient, Consumer<T> messageConsumer, ErrorListener errorListener) {
        this.chatClient = chatClient;
        this.errorListener = errorListener;
        this.consumer = messageConsumer;
    }

    @Override
    protected Object doInBackground(Void... params) {
        try {
            return chatClient.retrieveMessages();
        } catch (Exception e) {
            return e;
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        if (result instanceof Exception) {
            errorListener.onError();
        } else {
            consumer.accept((T) result);
        }
    }
}
