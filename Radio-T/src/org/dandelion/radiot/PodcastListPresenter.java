package org.dandelion.radiot;

import java.util.List;

import org.dandelion.radiot.PodcastListPresenter.UpdateProgress;
import org.dandelion.radiot.PodcastList.IModel;
import org.dandelion.radiot.PodcastList.IView;

import android.os.AsyncTask;

interface IPresenterInternal {
	void doInBackground(UpdateProgress result);

	void taskStarted();

	void taskFinished(UpdateProgress result);

	void taskCancelled();
}

public class PodcastListPresenter implements PodcastList.IPresenter {
	protected IModel model;
	protected IView view;
	private UpdateProgress lastResult;

	private PodcastListPresenter(PodcastList.IModel model) {
		this.model = model;
	}

	public void cancelUpdate() {
	}

	public void refreshData(boolean resetCache) {
		if (null == lastResult || resetCache) {
			forkWorkerThread();
		} else {
			lastResult.updateView(view);
		}
	}

	protected void forkWorkerThread() {
	}

	public void doInBackground(UpdateProgress progress) {
		progress.retrievePodcasts(model);
	}

	public void taskStarted() {
		view.showProgress();
	}

	public void taskFinished(UpdateProgress progress) {
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
	}

	public static class SyncPresenter extends PodcastListPresenter {

		public SyncPresenter(IModel model) {
			super(model);
		}

		@Override
		protected void forkWorkerThread() {
			UpdateProgress progress = new UpdateProgress();
			taskStarted();
			doInBackground(progress);
			taskFinished(progress);
		}
	}

	public static class AsyncPresenter extends PodcastListPresenter implements
			IPresenterInternal {
		private RefreshTask task;

		public AsyncPresenter(IModel model) {
			super(model);
		}

		@Override
		protected void forkWorkerThread() {
			if (!isRunningUpdate()) {
				task = new RefreshTask(this);
				task.execute(new UpdateProgress());
			}
		}

		private boolean isRunningUpdate() {
			return task != null;
		}

		@Override
		public void cancelUpdate() {
			if (isRunningUpdate()) {
				task.cancel(true);
				view.closeProgress();
			}
		}

		@Override
		public void taskFinished(UpdateProgress progress) {
			super.taskFinished(progress);
			task = null;
		}

		public void taskCancelled() {
			task = null;
		}
	}
}

class RefreshTask extends AsyncTask<UpdateProgress, Void, UpdateProgress> {

	private IPresenterInternal presenter;

	public RefreshTask(IPresenterInternal presenter) {
		this.presenter = presenter;
	}

	@Override
	protected UpdateProgress doInBackground(UpdateProgress... params) {
		UpdateProgress progress = params[0];
		presenter.doInBackground(progress);
		return progress;
	}

	@Override
	protected void onPostExecute(UpdateProgress progress) {
		presenter.taskFinished(progress);
	}

	@Override
	protected void onPreExecute() {
		presenter.taskStarted();
	}

	@Override
	protected void onCancelled() {
		presenter.taskCancelled();
	}
}
