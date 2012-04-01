package org.dandelion.radiot.live.core;

public class TimeoutScheduler implements Scheduler, Runnable {
    public static int WAIT_TIMEOUT = 60 * 1000;

    private Timeout timeout;
    private Performer performer;

    public TimeoutScheduler(Timeout timeout) {
        this.timeout = timeout;
    }

    @Override
    public void scheduleNextAttempt() {
        timeout.set(WAIT_TIMEOUT, this);
    }

    @Override
    public void cancelAttempts() {
        timeout.reset();
    }

    public void timeoutElapsed() {
        run();
    }

    @Override
    public void run() {
        performer.performNextAttempt();
    }

    public void setPerformer(Performer performer) {
        this.performer = performer;
    }
}
