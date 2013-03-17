package org.dandelion.radiot.live.schedule;

public class DeterministicScheduler implements Scheduler {
    private Performer performer;
    private int scheduleCount = 0;

    @Override
    public void setPerformer(Performer performer) {
        this.performer = performer;
    }

    @Override
    public void scheduleNext() {
        scheduleCount++;
    }

    @Override
    public void cancel() {
        if (isScheduled()) {
            scheduleCount--;
        }
    }

    public void performAction() {
        if (isScheduled() && performer != null) {
            scheduleCount--;
            performer.performAction();
        }
    }

    public boolean isScheduled() {
        return scheduleCount > 0;
    }

}
