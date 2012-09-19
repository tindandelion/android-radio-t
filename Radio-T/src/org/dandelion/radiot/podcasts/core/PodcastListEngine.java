package org.dandelion.radiot.podcasts.core;

import java.util.List;

import org.dandelion.radiot.podcasts.core.PodcastList.IModel;

import android.os.AsyncTask;

@SuppressWarnings("unchecked")
public class PodcastListEngine implements PodcastList.IPodcastListEngine {
	protected IModel model;
	protected ProgressListener view;
	protected UpdateTask task;
	private List<PodcastItem> currentPodcasts;
    private PodcastListConsumer consumer;

    public PodcastListEngine(PodcastList.IModel model) {
		this.model = model;
		view = new NullView();
	}

	public void refresh(boolean resetCache) {
		if (resetCache) {
			currentPodcasts = null;
		}
		
		if (null == currentPodcasts) {
			startRefreshTask();
		} else {
			updateViewWithCurrentPodcasts();
		}
	}

	protected void updateViewWithCurrentPodcasts() {
		consumer.updatePodcasts(currentPodcasts);
	}

	public void taskStarted() {
	}

	public void taskFinished() {
		task = null;
	}

	protected void publishPodcastList(List<PodcastItem> newList,
			Exception loadError) {
		view.onFinished();

		if (null != loadError) {
			view.onError(loadError.getMessage());
		} else {
			currentPodcasts = newList;
			updateViewWithCurrentPodcasts();
		}
	}

	public void detach() {
		view = new NullView();
        consumer = new NullConsumer();
	}

	public void attach(ProgressListener view, PodcastListConsumer consumer) {
		this.view = view;
        this.consumer = consumer;
	}

	protected boolean isInProgress() {
		return task != null;
	}

	protected void startRefreshTask() {
		view.onStarted();
		if (!isInProgress()) {
			task = new UpdateTask();
            task.execute();
		}
	}

	public void cancelUpdate() {
		if (isInProgress()) {
			task.cancel(true);
			view.onFinished();
		}
	}

	public void taskCancelled() {
		task = null;
	}

	class UpdateTask extends AsyncTask<Void, Runnable, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			List<PodcastItem> list = retrievePodcastList();
			if (null != list) {
				retrievePodcastImages(list);
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Runnable... values) {
			values[0].run();
		}

		@Override
		protected void onPostExecute(Void result) {
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

		public List<PodcastItem> retrievePodcastList() {
			List<PodcastItem> newList = null;
			Exception error = null;
			try {
				newList = model.retrievePodcasts();
			} catch (Exception e) {
				error = e;
			}
			publishProgress(updatePodcastsRunnable(newList, error));
			return newList;
		}

		protected Runnable updatePodcastsRunnable(
				final List<PodcastItem> newList, final Exception error) {
			return new Runnable() {
				public void run() {
					publishPodcastList(newList, error);
				}
			};
		}

		private void retrievePodcastImages(List<PodcastItem> list) {
			for (int i = 0; i < list.size(); i++) {
				PodcastItem item = list.get(i);
				final int index = i;

				item.setThumbnail(model.loadPodcastImage(item));
				publishProgress(new Runnable() {
					public void run() {
						consumer.updatePodcastImage(index);
					}
				});
			}
		}
	}
}

class NullConsumer implements PodcastListConsumer {
    @Override
    public void updatePodcasts(List<PodcastItem> podcasts) {
    }

    @Override
    public void updatePodcastImage(int index) {
    }
}

class NullView implements ProgressListener {

    public void onStarted() {
	}

	public void onFinished() {
	}

	public void onError(String errorMessage) {
	}

}
