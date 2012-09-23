package org.dandelion.radiot.podcasts.core;

import java.util.List;

import android.os.AsyncTask;

@SuppressWarnings("unchecked")
public class AsyncPodcastListLoader implements PodcastListLoader {
    protected PodcastsCache cache;
    private ProgressListener progressListener = new NullListener();
    private PodcastsProvider podcasts;
    private PodcastListConsumer consumer;
    private ThumbnailProvider thumbnails;
    private UpdateTask task;

    public AsyncPodcastListLoader(PodcastsProvider podcasts, ThumbnailProvider thumbnails) {
        this(podcasts, thumbnails, new MemoryCache());
    }

    public AsyncPodcastListLoader(PodcastsProvider podcasts, ThumbnailProvider thumbnails, PodcastsCache cache) {
        this.cache = cache;
        this.podcasts = new CachingPodcastProvider(podcasts, cache);
        this.thumbnails = thumbnails;
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

    protected void publishPodcastList(List<PodcastItem> newList,
                                      Exception loadError) {
        progressListener.onFinished();

        if (null != loadError) {
            progressListener.onError(loadError.getMessage());
        } else {
            consumer.updatePodcasts(newList);
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

        private List<PodcastItem> list;
        private Exception error;

        @Override
        protected Void doInBackground(Void... params) {
            retrievePodcastList();
            if (null != list) {
                retrievePodcastImages();
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

        public void retrievePodcastList() {
            try {
                list = podcasts.retrieveAll();
            } catch (Exception e) {
                error = e;
            }

        }

        private void retrievePodcastImages() {
            for (PodcastItem item : list) {
                item.setThumbnail(thumbnails.thumbnailFor(item));
            }
        }
    }
}


class NullConsumer implements PodcastListConsumer {
    @Override
    public void updatePodcasts(List<PodcastItem> podcasts) {
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
