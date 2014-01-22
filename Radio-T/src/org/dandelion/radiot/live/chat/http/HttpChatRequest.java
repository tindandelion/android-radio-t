package org.dandelion.radiot.live.chat.http;

import android.os.AsyncTask;
import org.dandelion.radiot.live.chat.Message;
import org.dandelion.radiot.live.chat.MessageConsumer;

import java.util.List;

@SuppressWarnings("unchecked")
public class HttpChatRequest extends AsyncTask<Void, Void, Object> {

    public interface ErrorListener {
        void onError();
    }

    private final HttpChatClient chatClient;
    private final ErrorListener errorListener;
    private final MessageConsumer messageConsumer;

    protected HttpChatRequest(HttpChatClient chatClient, MessageConsumer messageConsumer, ErrorListener errorListener) {
        this.chatClient = chatClient;
        this.errorListener = errorListener;
        this.messageConsumer = messageConsumer;
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
            messageConsumer.processMessages((List<Message>) result);
        }
    }
}
