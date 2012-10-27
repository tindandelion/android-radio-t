package org.dandelion.radiot.podcasts.loader;

import android.os.AsyncTask;
import org.dandelion.radiot.podcasts.core.PodcastList;

@SuppressWarnings("unchecked")
public class AsyncPodcastListLoader implements PodcastListLoader {
    private ProgressListener progressListener = ProgressListener.Null;
    private PodcastsConsumer consumer = PodcastsConsumer.Null;

    private CachingPodcastLoader podcasts;
    private UpdateTask task;

    public AsyncPodcastListLoader(PodcastsProvider podcasts, PodcastsCache cache) {
        this.podcasts = new CachingPodcastLoader(podcasts, cache);
    }

    @Override
    public void refreshFromServer() {
        startRefreshTask(true);
    }

    @Override
    public void refreshFromCache() {
        startRefreshTask(false);
    }

    public void taskFinished() {
        task = null;
    }

    @Override
    public void detach() {
        progressListener = ProgressListener.Null;
        consumer = PodcastsConsumer.Null;
    }

    @Override
    public void attach(ProgressListener listener, PodcastsConsumer consumer) {
        this.progressListener = listener;
        this.consumer = consumer;
    }


    protected boolean isInProgress() {
        return task != null;
    }

    protected void startRefreshTask(boolean resetCache) {
        if (!isInProgress()) {
            task = new UpdateTask(resetCache);
            task.execute();
        }
    }

    class UpdateTask extends AsyncTask<Void, PodcastList, Exception> implements PodcastsConsumer {
        private boolean resetCache;

        public UpdateTask(boolean resetCache) {
            this.resetCache = resetCache;
        }

        @Override
        protected Exception doInBackground(Void... params) {
            try {
                if (resetCache) {
                    podcasts.resetCache();
                }
                podcasts.retrieveTo(this);
            } catch (Exception e) {
                return e;
            }
            return null;
        }

        @Override
        public void updatePodcasts(PodcastList podcasts) {
            publishProgress(podcasts);
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

        private void finishSelf() {
            progressListener.onFinished();
            taskFinished();
        }

    }
}


