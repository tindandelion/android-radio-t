package org.dandelion.radiot.podcasts.download;

import java.util.HashMap;

public class DownloadTracker {

    public interface Listener {
        void onAllTasksCompleted();
    }

    public interface PostProcessor {
        void downloadComplete(DownloadTask task);
    }

    private Listener listener;
    private PostProcessor postProcessor;
    
    private HashMap<Long, DownloadTask> tasksInProgress;

    public DownloadTracker(PostProcessor post) {
        this.postProcessor = post;
        tasksInProgress = new HashMap<Long, DownloadTask>();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void taskScheduled(long id, DownloadTask task) {
        tasksInProgress.put(id, task);
    }

    public void taskCompleted(long id) {
        if (!tasksInProgress.containsKey(id)) {
            return;
        }

        DownloadTask completedTask = tasksInProgress.remove(id);
        postProcessor.downloadComplete(completedTask);

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
