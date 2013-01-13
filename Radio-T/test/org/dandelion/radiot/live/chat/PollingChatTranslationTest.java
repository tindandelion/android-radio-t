package org.dandelion.radiot.live.chat;

import org.dandelion.radiot.live.schedule.DeterministicScheduler;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class PollingChatTranslationTest {
    private ChatTranslation realTranslation = new FakeChatTranslation();
    private DeterministicScheduler scheduler = new DeterministicScheduler();
    private MessageConsumer consumer = mock(MessageConsumer.class);
    private PollingChatTranslation pollingTranslation = new PollingChatTranslation(realTranslation, scheduler);

    @Test
    public void startTranslation_DelegatesToRealTranslation() throws Exception {
        pollingTranslation.start(consumer);
        verify(consumer).initWithMessages(FakeChatTranslation.INITIAL_MESSAGES);
    }

    @Test
    public void startTranslation_SchedulesRefresh() throws Exception {
        pollingTranslation.start(consumer);
        scheduler.performAction();
        verify(consumer).appendMessages(FakeChatTranslation.SUBSEQUENT_MESSAGES);
    }

    @Test
    public void refreshIsScheduledRepeatedly() throws Exception {
        pollingTranslation.start(consumer);
        scheduler.performAction();
        verify(consumer).appendMessages(FakeChatTranslation.SUBSEQUENT_MESSAGES);

        reset(consumer);
        scheduler.performAction();
        verify(consumer).appendMessages(FakeChatTranslation.SUBSEQUENT_MESSAGES);
    }

    @Test @Ignore
    public void refreshIsCancelled() throws Exception {
    }

    private static class FakeChatTranslation implements ChatTranslation {
        private static final List<Message> INITIAL_MESSAGES = Collections.emptyList();
        private static final List<Message> SUBSEQUENT_MESSAGES = Collections.emptyList();
        private MessageConsumer consumer;

        @Override
        public void start(MessageConsumer consumer) {
            this.consumer = consumer;
            consumer.initWithMessages(INITIAL_MESSAGES);
        }

        @Override
        public void refresh() {
            consumer.appendMessages(SUBSEQUENT_MESSAGES);
        }
    }
}
