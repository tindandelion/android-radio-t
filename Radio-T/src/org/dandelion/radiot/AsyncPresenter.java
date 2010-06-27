package org.dandelion.radiot;

import java.util.List;

import org.dandelion.radiot.PodcastList.IModel;
import org.dandelion.radiot.PodcastList.IView;

import android.os.AsyncTask;

public class AsyncPresenter implements PodcastList.IPresenter {
	private IView view;
	private RefreshTask task;
	private IModel model;

	public AsyncPresenter(PodcastList.IModel model) {
		this.model = model;
	}

	public void initialize(PodcastList.IView view) {
		this.view = view;
	}

	public void refreshData() {
		task = new RefreshTask();
		task.execute();
	}

	class RefreshTask extends AsyncTask<Void, Void, Void> {

		private List<PodcastItem> podcasts;
		private String errorMessage;

		@Override
		protected void onPreExecute() {
			view.showProgress();
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

		private boolean success() {
			return null == errorMessage;
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				podcasts = model.retrievePodcasts();
			} catch (Exception e) {
				errorMessage = e.getMessage();
			}
			return null;
		}

	}
}
