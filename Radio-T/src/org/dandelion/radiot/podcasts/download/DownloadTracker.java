package org.dandelion.radiot.podcasts.download;

import java.util.HashMap;

public class DownloadTracker extends DownloadProcessor implements SystemDownloadMonitor.DownloadListener {
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

    @Override
    public void onDownloadComplete(long id) {
        if (!isTrackingTask(id)) return;

        processCompletedTask(id);
        if (!hasScheduledTasks()) {
            notifyListener();
        }
    }

    private void processCompletedTask(long id) {
        DownloadTask completedTask = tasksInProgress.remove(id);
        passFurther(completedTask);
    }

    private boolean isTrackingTask(long id) {
        return tasksInProgress.containsKey(id);
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
