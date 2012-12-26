package org.dandelion.radiot.live.schedule;

public interface Scheduler {
    public interface Performer {
        void performNextAttempt();
    }
    void setPerformer(Performer performer);
    void scheduleNextAttempt();
    void cancelAttempts();
}
