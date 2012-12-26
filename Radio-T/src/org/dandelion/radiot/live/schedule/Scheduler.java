package org.dandelion.radiot.live.schedule;

public interface Scheduler {
    public interface Performer {
        void performAction();
    }
    void setPerformer(Performer performer);
    void scheduleNext();
    void cancel();
}
