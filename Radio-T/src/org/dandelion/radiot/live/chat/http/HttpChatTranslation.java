package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.common.ui.Announcer;
import org.dandelion.radiot.live.chat.ChatTranslation;
import org.dandelion.radiot.live.chat.Message;
import org.dandelion.radiot.live.chat.MessageConsumer;
import org.dandelion.radiot.live.chat.ProgressListener;
import org.dandelion.radiot.live.schedule.Scheduler;

import java.util.List;

public class HttpChatTranslation implements ChatTranslation, MessageConsumer {

    private enum TranslationState {DISCONNECTED, CONNECTING, CONNECTED}

    private final Announcer<ProgressListener> progressAnnouncer = new Announcer<ProgressListener>(ProgressListener.class);
    private final Announcer<MessageConsumer> messageAnnouncer = new Announcer<MessageConsumer>(MessageConsumer.class);
    final HttpChatClient chatClient;
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
        state = TranslationState.CONNECTED;
        refreshScheduler.scheduleNext();
    }

    @Override
    public void processMessages(List<Message> messages) {
        consumeMessages(messages);
    }
}
