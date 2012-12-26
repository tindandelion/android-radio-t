package org.dandelion.radiot.live.schedule;

// TODO: Combine setPerformer() and scheduleNext() into one?
public interface Scheduler {
    public interface Performer {
        void performAction();
    }
    void setPerformer(Performer performer);
    void scheduleNext();
    void cancel();
}
