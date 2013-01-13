package org.dandelion.radiot.live.schedule;

public class DeterministicScheduler implements Scheduler {
    private boolean isScheduled = false;
    private Performer performer;

    @Override
    public void setPerformer(Performer performer) {
        this.performer = performer;
    }

    @Override
    public void scheduleNext() {
        isScheduled = true;
    }

    @Override
    public void cancel() {
        isScheduled = false;
    }

    public void performAction() {
        if (isScheduled && performer != null) {
            isScheduled = false;
            performer.performAction();
        }
    }

    public boolean isScheduled() {
        return isScheduled;
    }
}
