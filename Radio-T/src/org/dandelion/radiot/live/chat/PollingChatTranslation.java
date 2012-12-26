package org.dandelion.radiot.live.chat;

import org.dandelion.radiot.live.schedule.Scheduler;

public class PollingChatTranslation implements ChatTranslation {
    private final ChatTranslation translation;
    private final Scheduler scheduler;

    public PollingChatTranslation(ChatTranslation translation, Scheduler scheduler) {
        this.translation = translation;
        this.scheduler = scheduler;
    }

    @Override
    public void start(MessageConsumer consumer) {
        translation.start(consumer);
        scheduler.scheduleNextAttempt();
    }

    @Override
    public void refresh() {
        translation.refresh();
    }
}
