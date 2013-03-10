package org.dandelion.radiot.live.chat.http;

import android.os.AsyncTask;
import org.dandelion.radiot.live.chat.Message;
import org.dandelion.radiot.live.chat.MessageConsumer;
import org.dandelion.radiot.live.chat.ProgressListener;

import java.util.List;

public abstract class HttpChatRequest extends AsyncTask<Void, Void, List<Message>> {
    protected final ProgressListener progressListener;
    private final HttpChatClient chatClient;
    private final MessageConsumer messageConsumer;

    private Exception error;
    private String mode;

    protected HttpChatRequest(String mode, HttpChatClient chatClient, ProgressListener progressListener, MessageConsumer messageConsumer) {
        this.progressListener = progressListener;
        this.chatClient = chatClient;
        this.messageConsumer = messageConsumer;
        this.mode = mode;
    }

    @Override
    protected List<Message> doInBackground(Void... params) {
        try {
            return chatClient.retrieveMessages(mode);
        } catch (Exception e) {
            error = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Message> messages) {
        if (error != null) {
            reportError();
        } else {
            consumeMessages(messages);
        }
    }

    private void consumeMessages(List<Message> messages) {
        messageConsumer.processMessages(messages);

    }

    private void reportError() {
        progressListener.onError();
    }

    static class Last extends HttpChatRequest {
        Last(HttpChatClient chatClient, ProgressListener progressListener, MessageConsumer messageConsumer) {
            super("last", chatClient, progressListener, messageConsumer);
        }
    }

    static class Next extends HttpChatRequest {
        Next(HttpChatClient chatClient, ProgressListener progressListener, MessageConsumer messageConsumer) {
            super("next", chatClient, progressListener, messageConsumer);
        }
    }
}
