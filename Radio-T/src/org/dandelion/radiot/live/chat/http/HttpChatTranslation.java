package org.dandelion.radiot.live.chat.http;

import android.os.AsyncTask;
import org.dandelion.radiot.common.ui.Announcer;
import org.dandelion.radiot.live.chat.ChatTranslation;
import org.dandelion.radiot.live.chat.Message;
import org.dandelion.radiot.live.chat.MessageConsumer;
import org.dandelion.radiot.live.chat.ProgressListener;
import org.dandelion.radiot.live.schedule.Scheduler;

import java.util.List;

public class HttpChatTranslation implements ChatTranslation {
    private enum TranslationState {DISCONNECTED, CONNECTING, CONNECTED}

    private final Announcer<ProgressListener> progressAnnouncer = new Announcer<ProgressListener>(ProgressListener.class);
    private final Announcer<MessageConsumer> messageAnnouncer = new Announcer<MessageConsumer>(MessageConsumer.class);
    private final HttpChatClient chatClient;
    private final Scheduler refreshScheduler;
    private TranslationState state = TranslationState.DISCONNECTED;

    public HttpChatTranslation(String baseUrl, Scheduler refreshScheduler) {
        this(new HttpChatClient(baseUrl), refreshScheduler);
    }

    public HttpChatTranslation(HttpChatClient chatClient, Scheduler refreshScheduler) {
        this.chatClient = chatClient;
        this.refreshScheduler = refreshScheduler;
        refreshScheduler.setPerformer(new Scheduler.Performer() {
            @Override
            public void performAction() {
                requestNextMessages();
            }
        });
    }

    @Override
    public void setProgressListener(ProgressListener listener) {
        progressAnnouncer.setTarget(listener);
    }

    @Override
    public void setMessageConsumer(MessageConsumer consumer) {
        messageAnnouncer.setTarget(consumer);
    }

    @Override
    public void start() {
        if (state == TranslationState.DISCONNECTED) {
            requestLastMessages();
        } else if (state == TranslationState.CONNECTING) {
            progressAnnouncer.announce().onConnecting();
        } else if (state == TranslationState.CONNECTED) {
            scheduleRefresh();
        }
    }

    @Override
    public void shutdown() {
        setMessageConsumer(null);
        setProgressListener(null);
        chatClient.shutdown();
    }

    @Override
    public void stop() {
        refreshScheduler.cancel();
    }

    private void requestLastMessages() {
        state = TranslationState.CONNECTING;
        new LastRecordsRequest(this).execute();
    }

    private void requestNextMessages() {
        new NextRecordsRequest(this).execute();
    }

    private void consumeMessages(List<Message> messages) {
        messageAnnouncer.announce().processMessages(messages);
        scheduleRefresh();
    }

    private void scheduleRefresh() {
        state = TranslationState.CONNECTED;
        refreshScheduler.scheduleNext();
    }

    private ProgressListener announceProgress() {
        return progressAnnouncer.announce();
    }

    private static abstract class ChatTranslationTask extends AsyncTask<Void, Void, List<Message>> {
        protected final HttpChatTranslation translation;
        private Exception error;
        private String mode;

        protected ChatTranslationTask(HttpChatTranslation translation, String mode) {
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

    }


    private static class LastRecordsRequest extends ChatTranslationTask {
        public LastRecordsRequest(HttpChatTranslation translation) {
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

    private static class NextRecordsRequest extends ChatTranslationTask {
        private NextRecordsRequest(HttpChatTranslation translation) {
            super(translation, "next");
        }
    }
}
