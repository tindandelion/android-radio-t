package org.dandelion.radiot.live.chat;

import org.dandelion.radiot.live.schedule.Scheduler;
import org.dandelion.radiot.util.ProgrammerError;

public class PollingChatTranslation implements ChatTranslation, Scheduler.Performer {
    private final ChatTranslation translation;
    private final Scheduler scheduler;

    public PollingChatTranslation(ChatTranslation translation, Scheduler scheduler) {
        this.translation = translation;
        this.scheduler = scheduler;
        scheduler.setPerformer(this);
    }

    @Override
    public void start(MessageConsumer consumer) {
        translation.start(consumer);
        scheduler.scheduleNext();
    }

    @Override
    public void refresh() {
        throw new ProgrammerError("This method should never be called");
    }

    @Override
    public void performAction() {
        translation.refresh();
    }
}
