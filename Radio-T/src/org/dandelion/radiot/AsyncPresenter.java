package org.dandelion.radiot;

import java.util.List;

import org.dandelion.radiot.PodcastList.IModel;
import org.dandelion.radiot.PodcastList.IView;

import android.os.AsyncTask;

interface IPresenterInternal {
	void doInBackground(UpdateProgress result);

	void preExecute();

	void postExecute(UpdateProgress result);
}

public class AsyncPresenter implements PodcastList.IPresenter,
		IPresenterInternal {
	private IModel model;
	private RefreshTask task;
	private IView view;

	public AsyncPresenter(PodcastList.IModel model, IView view) {
		this.model = model;
		this.view = view;
	}

	public void cancelLoading() {
		task.cancel(true);
		view.closeProgress();
		view.close();
	}

	public void refreshData() {
		task = new RefreshTask(this);
		task.execute(new UpdateProgress());
	}

	public void doInBackground(UpdateProgress progress) {
		progress.retrievePodcasts(model);
	}

	public void preExecute() {
		view.showProgress();
	}

	public void postExecute(UpdateProgress progress) {
		view.closeProgress();
		progress.updateView(view);
	}
}

class UpdateProgress {
	private List<PodcastItem> podcasts;
	private String errorMessage;

	public boolean isSuccessful() {
		return null == errorMessage;
	}

	public void retrievePodcasts(IModel model) {
		try {
			podcasts = model.retrievePodcasts();
		} catch (Exception e) {
			errorMessage = e.getMessage();
		}
	}

	public void updateView(IView view) {
		if (isSuccessful()) {
			view.updatePodcasts(podcasts);
		} else {
			view.showErrorMessage(errorMessage);
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
		presenter.postExecute(progress);
	}

	@Override
	protected void onPreExecute() {
		presenter.preExecute();
	}
}
