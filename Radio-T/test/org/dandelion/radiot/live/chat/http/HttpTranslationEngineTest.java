package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.live.chat.MessageConsumer;
import org.dandelion.radiot.live.chat.ProgressListener;
import org.dandelion.radiot.live.schedule.Scheduler;
import org.dandelion.radiot.robolectric.RadiotRobolectricRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RadiotRobolectricRunner.class)
public class HttpTranslationEngineTest {
    private final Scheduler scheduler  = mock(Scheduler.class);
    private final HttpChatClient chatClient = mock(HttpChatClient.class);
    private final MessageConsumer consumer = mock(MessageConsumer.class);
    private final ProgressListener listener = mock(ProgressListener.class);
    private final HttpTranslationEngine engine =
            new HttpTranslationEngine(chatClient, consumer, listener, scheduler);

    @Test
    public void schedulePolling() throws Exception {
        Scheduler.Performer performer = mock(Scheduler.Performer.class);
        engine.schedulePoll(performer);

        verify(scheduler).setPerformer(performer);
        verify(scheduler).scheduleNext();
    }

    @Test
    public void cancelPolling() throws Exception {
        engine.cancelPoll();
        verify(scheduler).cancel();
    }
}
