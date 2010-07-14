package org.dandelion.radiot;

import java.util.List;

import org.dandelion.radiot.PodcastList.IModel;
import org.dandelion.radiot.PodcastList.IView;

import android.os.AsyncTask;

public class AsyncPresenter implements PodcastList.IPresenter {
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
		task = new RefreshTask();
		task.execute();
	}

	class RefreshTask extends AsyncTask<Void, Void, Void> {

		private String errorMessage;
		private List<PodcastItem> podcasts;

		@Override
		protected Void doInBackground(Void... params) {
			try {
				podcasts = model.retrievePodcasts();
			} catch (Exception e) {
				errorMessage = e.getMessage();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			view.closeProgress();
			if (success()) {
				view.updatePodcasts(podcasts);
			} else { 
				view.showErrorMessage(errorMessage);
			}
		}

		@Override
		protected void onPreExecute() {
			view.showProgress();
		}

		private boolean success() {
			return null == errorMessage;
		}

	}
}
