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
    private ErrorListener errorListener = mock(ErrorListener.class);
    private final FakeChatTranslation realTranslation = new FakeChatTranslation();
    private ChatTranslation pollingTranslation = new PollingChatTranslation(realTranslation, scheduler);

    @Test
    public void startTranslation_DelegatesToRealTranslation() throws Exception {
        pollingTranslation.start(consumer, errorListener);
        verify(consumer).appendMessages(INITIAL_MESSAGES);
    }

    @Test
    public void startTranslation_SchedulesRefresh() throws Exception {
        pollingTranslation.start(consumer, errorListener);

        reset(consumer);
        scheduler.performAction();
        verify(consumer).appendMessages(SUBSEQUENT_MESSAGES);
    }

    @Test
    public void refreshIsScheduledRepeatedly() throws Exception {
        pollingTranslation.start(consumer, errorListener);

        reset(consumer);
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

    @Test
    public void onError_callsListenerAndCancelsRefresh() throws Exception {
        realTranslation.throwsError = true;
        pollingTranslation.start(consumer, errorListener);

        verify(errorListener).onError();
        assertFalse(scheduler.isScheduled());
    }

    private static class FakeChatTranslation implements ChatTranslation {
        private MessageConsumer consumer;
        public boolean throwsError = false;

        @Override
        public void start(MessageConsumer consumer, ErrorListener errorListener) {
            this.consumer = consumer;
            if (throwsError) {
                errorListener.onError();
            } else {
                consumer.appendMessages(INITIAL_MESSAGES);
            }

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
