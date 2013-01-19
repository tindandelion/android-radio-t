package org.dandelion.radiot.live.chat;

import org.dandelion.radiot.live.schedule.DeterministicScheduler;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

public class PollingChatTranslationTest {
    private static final List<Message> INITIAL_MESSAGES = Collections.emptyList();
    private static final List<Message> SUBSEQUENT_MESSAGES = Collections.emptyList();

    private DeterministicScheduler scheduler = new DeterministicScheduler();
    private MessageConsumer consumer = mock(MessageConsumer.class);
    private ChatTranslation.ErrorListener errorListener = mock(ChatTranslation.ErrorListener.class);
    private ChatTranslation pollingTranslation = new PollingChatTranslation(new FakeChatTranslation(), scheduler);

    @Test
    public void startTranslation_DelegatesToRealTranslation() throws Exception {
        pollingTranslation.start(consumer, errorListener);
        verify(consumer).initWithMessages(INITIAL_MESSAGES);
    }

    @Test
    public void startTranslation_SchedulesRefresh() throws Exception {
        pollingTranslation.start(consumer, errorListener);
        scheduler.performAction();
        verify(consumer).appendMessages(SUBSEQUENT_MESSAGES);
    }

    @Test
    public void refreshIsScheduledRepeatedly() throws Exception {
        pollingTranslation.start(consumer, errorListener);
        scheduler.performAction();
        verify(consumer).appendMessages(SUBSEQUENT_MESSAGES);

        reset(consumer);
        scheduler.performAction();
        verify(consumer).appendMessages(SUBSEQUENT_MESSAGES);
    }

    @Test
    public void refreshIsCancelled() throws Exception {
        pollingTranslation.start(consumer, errorListener);
        pollingTranslation.stop();

        assertFalse(scheduler.isScheduled());
    }

    private static class FakeChatTranslation implements ChatTranslation {
        private MessageConsumer consumer;

        @Override
        public void start(MessageConsumer consumer, ErrorListener errorListener) {
            this.consumer = consumer;
            consumer.initWithMessages(INITIAL_MESSAGES);
        }

        @Override
        public void refresh() {
            consumer.appendMessages(SUBSEQUENT_MESSAGES);
        }

        @Override
        public void stop() {
        }
    }
}
