package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.live.chat.MessageConsumer;
import org.dandelion.radiot.live.chat.ProgressListener;
import org.dandelion.radiot.live.schedule.Scheduler;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class HttpTranslationEngineTest {
    private final Scheduler scheduler  = mock(Scheduler.class);
    private final HttpChatClient chatClient = mock(HttpChatClient.class);
    private final MessageConsumer consumer = mock(MessageConsumer.class);
    private final ProgressListener listener = mock(ProgressListener.class);


    @Test
    public void schedulePolling() throws Exception {
        HttpTranslationEngine engine = new HttpTranslationEngine(chatClient, consumer, listener, scheduler);
        Scheduler.Performer performer = mock(Scheduler.Performer.class);
        engine.schedulePoll(performer);

        verify(scheduler).setPerformer(performer);
        verify(scheduler).scheduleNext();
    }
}
