package org.dandelion.radiot;

import java.util.List;

import org.dandelion.radiot.PodcastList.IModel;
import org.dandelion.radiot.PodcastList.IView;

import android.os.AsyncTask;

public class PodcastListPresenter implements PodcastList.IPresenter {
	protected IModel model;
	protected IView view;
	protected UpdateTask task;
	private List<PodcastItem> currentPodcasts;

	public PodcastListPresenter(PodcastList.IModel model) {
		this.model = model;
		view = new NullView();
	}

	public void refresh(boolean resetCache) {
		if (null == currentPodcasts || resetCache) {
			startRefreshTask();
		} else {
			updateViewWithCurrentPodcasts();
		}
	}

	protected void updateViewWithCurrentPodcasts() {
		view.updatePodcasts(currentPodcasts);
	}

	public void taskStarted() {
	}

	public void taskFinished() {
		task = null;
	}

	protected void publishPodcastList(List<PodcastItem> newList,
			Exception loadError) {
		view.closeProgress();

		if (null != loadError) {
			view.showErrorMessage(loadError.getMessage());
		} else {
			currentPodcasts = newList;
			updateViewWithCurrentPodcasts();
		}
	}

	public void detach() {
		view = new NullView();
	}

	public void attach(IView view) {
		this.view = view;
	}

	protected boolean isInProgress() {
		return task != null;
	}

	protected void startRefreshTask() {
		view.showProgress();
		if (!isInProgress()) {
			task = new UpdateTask();
			task.execute();
		}
	}

	public void cancelUpdate() {
		if (isInProgress()) {
			task.cancel(true);
			view.closeProgress();
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

				item.setImage(model.loadPodcastImage(item));
				publishProgress(new Runnable() {
					public void run() {
						view.updatePodcastImage(index);
					}
				});
			}
		}
	}
}

class NullView implements IView {

	public void updatePodcasts(List<PodcastItem> podcasts) {
	}

	public void showProgress() {
	}

	public void closeProgress() {
	}

	public void showErrorMessage(String errorMessage) {
	}

	public void updatePodcastImage(int index) {
	}
}
