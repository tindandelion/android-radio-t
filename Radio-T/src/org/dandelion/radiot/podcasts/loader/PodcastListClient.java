package org.dandelion.radiot.podcasts.loader;

import android.os.AsyncTask;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;

@SuppressWarnings("unchecked")
public class PodcastListClient {
    private ProgressListener progressListener = ProgressListener.Null;
    private PodcastsConsumer consumer = PodcastsConsumer.Null;

    private ThumbnailProvider thumbnails;

    private UpdateTask task;
    private PodcastsCache cache;
    private PodcastsProvider podcasts;

    public PodcastListClient(PodcastsProvider podcasts, PodcastsCache cache, ThumbnailProvider thumbnails) {
        this.thumbnails = thumbnails;
        this.podcasts = podcasts;
        this.cache = cache;
    }

    public void refreshFromServer() {
        cache.reset();
        startRefreshTask();
    }

    public void refreshFromCache() {
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

    class UpdateTask extends AsyncTask<Void, Runnable, Exception> implements PodcastsConsumer {
        private PodcastList list;

        public UpdateTask() {
        }

        @Override
        protected Exception doInBackground(Void... params) {
            try {
                retrievePodcastList();
                retrieveThumbnails();
            } catch (Exception e) {
                return e;
            }
            return null;
        }

        private void retrievePodcastList() throws Exception {
            new CachingPodcastLoader(podcasts, cache, this).retrieve();
        }

        private void retrieveThumbnails() {
            new ThumbnailLoader(list, thumbnails, this).retrieve();
        }

        @Override
        public void updateList(final PodcastList pl) {
            this.list = pl;
            publishProgress(new Runnable() {
                @Override
                public void run() {
                    consumer.updateList(pl);
                }
            });
        }

        @Override
        public void updateThumbnail(final PodcastItem item, final byte[] thumbnail) {
            publishProgress(new Runnable() {
                @Override
                public void run() {
                    consumer.updateThumbnail(item, thumbnail);
                }
            });
        }


        @Override
        protected void onProgressUpdate(Runnable... values) {
            values[0].run();
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


