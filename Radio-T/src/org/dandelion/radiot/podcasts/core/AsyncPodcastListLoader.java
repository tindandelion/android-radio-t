package org.dandelion.radiot.podcasts.core;

import android.os.AsyncTask;

@SuppressWarnings("unchecked")
public class AsyncPodcastListLoader implements PodcastListLoader {
    protected PodcastsCache cache;
    private ProgressListener progressListener = new NullListener();
    private PodcastsProvider podcasts;
    private PodcastListConsumer consumer;
    private UpdateTask task;

    public AsyncPodcastListLoader(PodcastsProvider podcasts) {
        this(podcasts, new MemoryCache());
    }

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

    public void taskStarted() {
    }

    public void taskFinished() {
        task = null;
    }

    protected void publishPodcastList(PodcastList list,
                                      Exception loadError) {
        progressListener.onFinished();

        if (null != loadError) {
            progressListener.onError(loadError.getMessage());
        } else {
            consumer.updatePodcasts(list);
        }
    }

    public void detach() {
        progressListener = new NullListener();
        consumer = new NullConsumer();
    }

    public void attach(ProgressListener listener, PodcastListConsumer consumer) {
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

    public void taskCancelled() {
        task = null;
    }

    class UpdateTask extends AsyncTask<Void, Runnable, Void> {

        private PodcastList list;
        private Exception error;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                list = podcasts.retrieveAll();
            } catch (Exception e) {
                error = e;
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Runnable... values) {
            values[0].run();
        }

        @Override
        protected void onPostExecute(Void result) {
            publishPodcastList(list, error);
            taskFinished();
        }

        @Override
        protected void onPreExecute() {
            taskStarted();
        }

        @Override
        protected void onCancelled() {
            taskCancelled();
        }
    }
}


class NullConsumer implements PodcastListConsumer {
    @Override
    public void updatePodcasts(PodcastList podcasts) {
    }

}

class NullListener implements ProgressListener {

    public void onStarted() {
    }

    public void onFinished() {
    }

    public void onError(String errorMessage) {
    }

}
