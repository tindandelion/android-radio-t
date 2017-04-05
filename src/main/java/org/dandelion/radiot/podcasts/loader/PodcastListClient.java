package org.dandelion.radiot.podcasts.loader;

import android.os.AsyncTask;
import org.dandelion.radiot.http.HttpClient;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;
import org.dandelion.radiot.podcasts.ui.PodcastListModel;

@SuppressWarnings("unchecked")
public class PodcastListClient implements PodcastListModel {
    private ProgressListener progressListener = ProgressListener.Null;
    private PodcastsConsumer consumer = PodcastsConsumer.Null;

    private UpdateTask task;
    private final PodcastListRetriever podcastRetriever;
    private final ThumbnailRetriever thumbnailRetriever;

    public PodcastListClient(PodcastsProvider podcasts, PodcastsCache podcastCache, HttpClient thumbnailClient, ThumbnailCache thumbnailCache) {
        podcastRetriever = new PodcastListRetriever(podcasts, podcastCache);
        thumbnailRetriever = new ThumbnailRetriever(thumbnailClient, thumbnailCache);
    }

    public void refreshData() {
        startRefreshTask(true);
    }

    public void populateConsumer() {
        startRefreshTask(false);
    }

    private void taskFinished() {
        task = null;
    }

    public void release() {
        cancelCurrentTask();
        progressListener = ProgressListener.Null;
        consumer = PodcastsConsumer.Null;
    }

    private void cancelCurrentTask() {
        if (isInProgress()) {
            task.cancel(true);
        }
    }

    public void attach(ProgressListener listener, PodcastsConsumer consumer) {
        this.progressListener = listener;
        this.consumer = consumer;
    }


    private boolean isInProgress() {
        return task != null;
    }

    private void startRefreshTask(boolean forceRefresh) {
        if (!isInProgress()) {
            task = new UpdateTask();
            task.execute(forceRefresh);
        }
    }

    class UpdateTask extends AsyncTask<Boolean, Runnable, Exception> implements PodcastsConsumer {
        private PodcastList list;

        UpdateTask() {
        }

        @Override
        protected Exception doInBackground(Boolean... params) {
            try {
                retrievePodcastList(params[0]);
                retrieveThumbnails();
            } catch (Exception e) {
                return e;
            }
            return null;
        }

        private void retrievePodcastList(Boolean forceRefresh) throws Exception {
            podcastRetriever.retrieveTo(this, forceRefresh);
        }

        private void retrieveThumbnails() {
            thumbnailRetriever.retrieve(list, this, interrupter());
        }

        private ThumbnailRetriever.Controller interrupter() {
            return new ThumbnailRetriever.Controller() {

                @Override
                public boolean isInterrupted() {
                    return isCancelled();
                }
            };
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
        protected void onCancelled() {
            finishSelf();
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


