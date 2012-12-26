package org.dandelion.radiot.live.core;

import org.dandelion.radiot.live.schedule.Scheduler;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TimeoutSchedulerTest {
    private Timeout timeout = mock(Timeout.class);
    private final TimeoutScheduler scheduler = new TimeoutScheduler(timeout);
    private Scheduler.Performer performer = mock(Scheduler.Performer.class);

    @Before
    public void setUp() throws Exception {
        scheduler.setPerformer(performer);
    }

    @Test
    public void scheduleNextAttempt() throws Exception {
        scheduler.scheduleNext();
        verify(timeout).set(TimeoutScheduler.WAIT_TIMEOUT);
    }

    @Test
    public void cancelAttempts() throws Exception {
        scheduler.cancel();
        verify(timeout).reset();
    }

    @Test
    public void callsPlayerWhenTimeoutElapses() throws Exception {
        scheduler.timeoutElapsed();
        verify(performer).performAction();
    }
}
