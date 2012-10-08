package org.dandelion.radiot.podcasts.core;

import android.os.AsyncTask;

@SuppressWarnings("unchecked")
public class AsyncPodcastListLoader implements PodcastListLoader {
    private ProgressListener progressListener = ProgressListener.Null;
    private PodcastsConsumer consumer = PodcastsConsumer.Null;

    protected PodcastsCache cache;
    private PodcastsProvider podcasts;
    private UpdateTask task;

    public AsyncPodcastListLoader(PodcastsProvider podcasts, PodcastsCache cache) {
        this.cache = cache;
        this.podcasts = new CachingPodcastProvider(podcasts, cache);
    }

    public void refresh(boolean resetCache) {
        if (resetCache) {
            cache.reset();
        }
        startRefreshTask();
    }

    public void taskFinished() {
        task = null;
    }

    public void detach() {
        progressListener = ProgressListener.Null;
        consumer = PodcastsConsumer.Null;
    }

    public void attach(ProgressListener listener, PodcastsConsumer consumer) {
        this.progressListener = listener;
        this.consumer = consumer;
    }

    protected boolean isInProgress() {
        return task != null;
    }

    protected void startRefreshTask() {
        if (!isInProgress()) {
            task = new UpdateTask();
            task.execute();
        }
    }

    public void cancelUpdate() {
        if (isInProgress()) {
            task.cancel(true);
        }
    }

    class UpdateTask extends AsyncTask<Void, PodcastList, Exception> {
        @Override
        protected Exception doInBackground(Void... params) {
            try {
                PodcastList pl = podcasts.retrieve();
                publishProgress(pl);
            } catch (Exception e) {
                return e;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(PodcastList... values) {
            consumer.updatePodcasts(values[0]);
        }

        @Override
        protected void onPostExecute(Exception ex) {
            finishSelf();
            publishError(ex);
        }

        private void publishError(Exception ex) {
            if (ex != null) {
                progressListener.onError(ex.getMessage());
            }
        }

        @Override
        protected void onPreExecute() {
            progressListener.onStarted();
        }

        @Override
        protected void onCancelled() {
            finishSelf();
        }

        private void finishSelf() {
            progressListener.onFinished();
            taskFinished();
        }
    }
}


