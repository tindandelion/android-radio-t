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

    protected void publishPodcastList(PodcastList list,
                                      Exception loadError) {

        if (null != loadError) {
            progressListener.onError(loadError.getMessage());
        } else {
            consumer.updatePodcasts(list);
        }
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
        progressListener.onStarted();
        if (!isInProgress()) {
            task = new UpdateTask();
            task.execute();
        }
    }

    public void cancelUpdate() {
        if (isInProgress()) {
            task.cancel(true);
            progressListener.onFinished();
        }
    }

    class UpdateTask extends AsyncTask<Void, Void, PodcastList> {
        private Exception error;

        @Override
        protected PodcastList doInBackground(Void... params) {
            try {
                return podcasts.retrieve();
            } catch (Exception e) {
                error = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(PodcastList list) {
            finishSelf();
            publishPodcastList(list, error);
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


