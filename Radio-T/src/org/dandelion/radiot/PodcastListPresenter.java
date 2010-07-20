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

public class PodcastListPresenter implements PodcastList.IPresenter, IPresenterInternal {
	protected IModel model;
	protected IView view;
	private UpdateProgress lastResult;
	protected RefreshTask task;

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

	public void doInBackground(UpdateProgress progress) {
		progress.retrievePodcasts(model);
	}

	public void taskStarted() {
	}

	public void taskFinished(UpdateProgress progress) {
		view.closeProgress();
		progress.updateView(view);
		if (progress.isSuccessful()) {
			lastResult = progress;
		}
		task = null;
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
			task = new RefreshTask(this);
			task.execute(new UpdateProgress());
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
