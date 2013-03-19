package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.live.chat.ChatTranslation;
import org.dandelion.radiot.live.chat.MessageConsumer;
import org.dandelion.radiot.live.chat.ProgressListener;
import org.dandelion.radiot.live.schedule.Scheduler;

public class HttpChatTranslation implements ChatTranslation {
    private HttpTranslationEngine engine;

    public HttpChatTranslation(String baseUrl, Scheduler refreshScheduler) {
        this(new HttpChatClient(baseUrl), refreshScheduler);
    }

    public HttpChatTranslation(HttpChatClient chatClient, Scheduler pollScheduler) {
        engine = new HttpTranslationEngine(chatClient, pollScheduler);
    }

    @Override
    public void setProgressListener(ProgressListener listener) {
        engine.setProgressListener(listener);
    }

    @Override
    public void setMessageConsumer(MessageConsumer consumer) {
        engine.setMessageConsumer(consumer);
    }

    @Override
    public void start() {
        engine.currentState().onStart();
    }

    @Override
    public void stop() {
        engine.currentState().onStop();
    }

    @Override
    public void shutdown() {
        engine.shutdown();
    }
}
