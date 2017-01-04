package org.dandelion.radiot.live.schedule;

import org.dandelion.radiot.common.Scheduler;
import org.dandelion.radiot.util.ProgrammerError;

public class DeterministicScheduler implements Scheduler {
    private Performer performer;
    private boolean isScheduled = false;

    @Override
    public void setPerformer(Performer performer) {
        this.performer = performer;
    }

    @Override
    public void scheduleNext() {
        if (isScheduled()) {
            throw new ProgrammerError("Previous task hasn't finished yet");
        }
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
