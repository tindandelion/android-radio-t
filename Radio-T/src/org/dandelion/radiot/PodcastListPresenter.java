package org.dandelion.radiot;

import java.util.List;

import org.dandelion.radiot.PodcastList.IModel;
import org.dandelion.radiot.PodcastList.IView;
import org.dandelion.radiot.PodcastListPresenter.UpdateProgress;

import android.os.AsyncTask;

interface IPresenterInternal {
	void retrievePodcastList(UpdateProgress result);
	void taskStarted();
	void taskFinished();
	void taskCancelled();
}

public class PodcastListPresenter implements PodcastList.IPresenter, IPresenterInternal {
	protected IModel model;
	protected IView view;
	private UpdateProgress lastResult;
	protected UpdateTask task;

	public PodcastListPresenter(PodcastList.IModel model) {
		this.model = model;
	}

	public void refreshData(boolean resetCache) {
		if (null == lastResult || resetCache) {
			view.showProgress();
			forkWorkerThread();
		} else {
			lastResult.updateView(view);
		}
	}

	public void retrievePodcastList(UpdateProgress progress) {
		progress.retrievePodcasts(model);
	}

	public void taskStarted() {
	}

	public void taskFinished() {
		task = null;
	}

	protected void publishPodcastList(UpdateProgress progress) {
		view.closeProgress();
		progress.updateView(view);
		if (progress.isSuccessful()) {
			lastResult = progress;
		}
	}

	public void detach() {
		view = null;
	}

	public void attach(IView view) {
		this.view = view;
	}

	protected boolean isRunningUpdate() {
		return task != null;
	}

	protected void forkWorkerThread() {
		if (!isRunningUpdate()) {
			task = new UpdateTask();
			task.execute();
		}
	}
	
	public void cancelUpdate() {
		if (isRunningUpdate()) {
			task.cancel(true);
			view.closeProgress();
		}
	}

	public void taskCancelled() {
		task = null;
	}


	public static class UpdateProgress {
		private List<PodcastItem> podcasts;
		private Exception error;

		public boolean isSuccessful() {
			return null == error;
		}

		public void retrievePodcasts(IModel model) {
			try {
				podcasts = model.retrievePodcasts();
			} catch (Exception e) {
				error = e;
			}
		}

		public void updateView(IView view) {
			if (isSuccessful()) {
				view.updatePodcasts(podcasts);
			} else {
				view.showErrorMessage(error.getMessage());
			}
		}

		public void retrievePodcastImages(IModel model) {
			for (PodcastItem item : podcasts) {
				item.setImage(model.loadPodcastImage(item));
			}
		}

		public List<PodcastItem> getPodcastList() {
			return podcasts;
		}
	}
	class UpdateTask extends AsyncTask<Void, Runnable, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			final UpdateProgress progress = new UpdateProgress();
			retrievePodcastList(progress);
			publishProgress(new Runnable() {
				public void run() {
					publishPodcastList(progress);
				}
			});
			retrievePodcastImages(progress);
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
		public void retrievePodcastImages(UpdateProgress progress) {
			List<PodcastItem> podcasts = progress.getPodcastList();
			for (int i = 0; i < podcasts.size(); i++) {
				PodcastItem item = podcasts.get(i);
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

