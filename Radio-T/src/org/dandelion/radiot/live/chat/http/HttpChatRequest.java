package org.dandelion.radiot.live.chat.http;

import android.os.AsyncTask;
import org.dandelion.radiot.live.chat.Message;

import java.util.List;

public abstract class HttpChatRequest extends AsyncTask<Void, Void, List<Message>> {
    protected final HttpChatTranslation translation;
    private Exception error;
    private String mode;

    HttpChatRequest(HttpChatTranslation translation, String mode) {
        this.translation = translation;
        this.mode = mode;
    }

    @Override
    protected List<Message> doInBackground(Void... params) {
        try {
            return translation.chatClient.retrieveMessages(mode);
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
            reportSuccess();
            consumeMessages(messages);
        }
    }

    private void consumeMessages(List<Message> messages) {
        translation.consumeMessages(messages);

    }

    private void reportError() {
        translation.announceProgress().onError();
    }

    protected void reportSuccess() {
    }

    static class Last extends HttpChatRequest {
        public Last(HttpChatTranslation translation) {
            super(translation, "last");
        }

        @Override
        protected void onPreExecute() {
            translation.announceProgress().onConnecting();
        }

        @Override
        protected void reportSuccess() {
            translation.announceProgress().onConnected();
        }
    }

    static class Next extends HttpChatRequest {
        public Next(HttpChatTranslation translation) {
            super(translation, "next");
        }
    }
}
