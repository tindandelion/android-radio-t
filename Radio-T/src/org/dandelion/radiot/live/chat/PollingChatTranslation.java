package org.dandelion.radiot.live.chat;

import org.dandelion.radiot.live.schedule.Scheduler;
import org.dandelion.radiot.util.ProgrammerError;

import java.util.List;

public class PollingChatTranslation implements ChatTranslation, Scheduler.Performer, MessageConsumer {
    private final ChatTranslation translation;
    private final Scheduler scheduler;
    private MessageConsumer consumer;

    public PollingChatTranslation(ChatTranslation translation, Scheduler scheduler) {
        this.translation = translation;
        this.scheduler = scheduler;
        scheduler.setPerformer(this);
    }

    @Override
    public void start(MessageConsumer consumer, ProgressListener progressListener) {
        // TODO: Deal with errors
        this.consumer = consumer;
        translation.start(this, progressListener);
    }

    @Override
    public void refresh() {
        throw new ProgrammerError("This method should never be called");
    }

    @Override
    public void stop() {
        consumer = null;
        translation.stop();
        scheduler.cancel();
    }

    @Override
    public void performAction() {
        translation.refresh();
    }

    @Override
    public void appendMessages(List<Message> messages) {
        consumer.appendMessages(messages);
        scheduler.scheduleNext();
    }
}
