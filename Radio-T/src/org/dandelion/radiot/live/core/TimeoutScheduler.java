package org.dandelion.radiot.live.core;

import org.dandelion.radiot.live.schedule.Scheduler;

public class TimeoutScheduler implements Scheduler {
    public static int WAIT_TIMEOUT = 60 * 1000;

    private Timeout timeout;
    private Performer performer;

    public TimeoutScheduler(Timeout timeout) {
        this.timeout = timeout;
    }

    @Override
    public void scheduleNextAttempt() {
        timeout.set(WAIT_TIMEOUT);
    }

    @Override
    public void cancelAttempts() {
        timeout.reset();
    }

    public void timeoutElapsed() {
        performer.performNextAttempt();
    }

    public void setPerformer(Performer performer) {
        this.performer = performer;
    }
}
