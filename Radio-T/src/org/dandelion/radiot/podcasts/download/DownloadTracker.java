package org.dandelion.radiot.podcasts.download;

import java.util.HashMap;

public class DownloadTracker extends DownloadProcessor {
    public interface Listener {
        void onAllTasksCompleted();
    }

    private Listener listener;
    private HashMap<Long, DownloadTask> tasksInProgress;

    public DownloadTracker(DownloadProcessor next) {
        super(next);
        tasksInProgress = new HashMap<Long, DownloadTask>();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void acceptTask(DownloadTask task) {
        tasksInProgress.put(task.id, task);
    }

    public void taskCompleted(long id) {
        if (!tasksInProgress.containsKey(id)) {
            return;
        }

        DownloadTask completedTask = tasksInProgress.remove(id);
        passFurther(completedTask);

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
