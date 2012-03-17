package org.dandelion.radiot.podcasts.download;

import java.util.ArrayList;

public class DownloadTracker {
    public interface Listener {
        void onAllTasksCompleted();
    }

    private Listener listener;
    private ArrayList<Long> tasksInProgress;

    public DownloadTracker() {
        tasksInProgress = new ArrayList<Long>();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void taskScheduled(long id) {
        tasksInProgress.add(id);
    }

    public void taskCompleted(long id) {
        tasksInProgress.remove(new Long(id));
        if (!hasScheduledTasks()) {
            notifyListener();
        }
    }

    private void notifyListener() {
        if (listener != null) {
            listener.onAllTasksCompleted();
        }
    }

    public boolean hasScheduledTasks() {
        return !tasksInProgress.isEmpty();
    }

}
