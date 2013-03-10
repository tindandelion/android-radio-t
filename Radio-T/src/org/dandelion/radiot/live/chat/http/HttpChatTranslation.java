package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.common.ui.Announcer;
import org.dandelion.radiot.live.chat.ChatTranslation;
import org.dandelion.radiot.live.chat.Message;
import org.dandelion.radiot.live.chat.MessageConsumer;
import org.dandelion.radiot.live.chat.ProgressListener;
import org.dandelion.radiot.live.schedule.Scheduler;

import java.util.List;

public class HttpChatTranslation implements ChatTranslation, MessageConsumer {
    private static class HttpTranslationState {
        protected final HttpChatTranslation translation;

        private HttpTranslationState(HttpChatTranslation translation) {
            this.translation = translation;
        }

        public void onStart() {
        }

        public void onStop() {
        }

        static class Disconnected extends HttpTranslationState {
            public Disconnected(HttpChatTranslation translation) {
                super(translation);
            }

            @Override
            public void onStart() {
                this.translation.setCurrentState(new Connecting(this.translation));
                this.translation.progressAnnouncer.announce().onConnecting();
                this.translation.requestLastMessages();
            }
        }

        static class Connecting extends HttpTranslationState {
            public Connecting(HttpChatTranslation translation) {
                super(translation);
            }

            @Override
            public void onStart() {
                this.translation.setCurrentState(this);
                this.translation.progressAnnouncer.announce().onConnecting();
            }
        }

        static class Connected extends HttpTranslationState {
            public Connected(HttpChatTranslation translation) {
                super(translation);
            }

            @Override
            public void onStart() {
                this.translation.setCurrentState(this);
                this.translation.scheduleRefresh();
            }

            @Override
            public void onStop() {
                this.translation.cancelRefresh();
            }
        }
    }

    private final Announcer<ProgressListener> progressAnnouncer = new Announcer<ProgressListener>(ProgressListener.class);
    private final Announcer<MessageConsumer> messageAnnouncer = new Announcer<MessageConsumer>(MessageConsumer.class);
    final HttpChatClient chatClient;
    private final Scheduler refreshScheduler;
    private HttpTranslationState currentState = new HttpTranslationState.Disconnected(this);

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
        currentState.onStart();
    }

    @Override
    public void stop() {
        currentState.onStop();
    }

    private void cancelRefresh() {
        refreshScheduler.cancel();
    }

    @Override
    public void shutdown() {
        setMessageConsumer(null);
        setProgressListener(null);
        chatClient.shutdown();
    }


    private void setCurrentState(HttpTranslationState state) {
        this.currentState = state;
    }

    private void requestLastMessages() {
        new HttpChatRequest.Last(chatClient, progressAnnouncer.announce(), this).execute();
    }

    private void requestNextMessages() {
        new HttpChatRequest.Next(chatClient, progressAnnouncer.announce(), this).execute();
    }

    void consumeMessages(List<Message> messages) {
        messageAnnouncer.announce().processMessages(messages);
        scheduleRefresh();
    }

    private void scheduleRefresh() {
        setCurrentState(new HttpTranslationState.Connected(this));
        refreshScheduler.scheduleNext();
    }

    @Override
    public void processMessages(List<Message> messages) {
        consumeMessages(messages);
    }
}
