package org.dandelion.radiot.live.chat.http;

import android.os.AsyncTask;
import org.dandelion.radiot.live.chat.Message;
import org.dandelion.radiot.live.chat.MessageConsumer;
import org.dandelion.radiot.live.chat.ProgressListener;

import java.util.List;

@SuppressWarnings("unchecked")
public class HttpChatRequest extends AsyncTask<Void, Void, Object> {
    protected final ProgressListener progressListener;
    private final HttpChatClient chatClient;
    private final MessageConsumer messageConsumer;
    private String mode;

    protected HttpChatRequest(String mode, HttpChatClient chatClient, ProgressListener progressListener, MessageConsumer messageConsumer) {
        this.progressListener = progressListener;
        this.chatClient = chatClient;
        this.messageConsumer = messageConsumer;
        this.mode = mode;
    }

    @Override
    protected Object doInBackground(Void... params) {
        try {
            return chatClient.retrieveMessages(mode);
        } catch (Exception e) {
            return e;
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        if (result instanceof Exception) {
            progressListener.onError();
        } else {
            messageConsumer.processMessages((List<Message>) result);
        }
    }
}
