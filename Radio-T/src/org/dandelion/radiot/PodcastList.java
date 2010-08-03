package org.dandelion.radiot;

import java.util.List;

import android.graphics.Bitmap;

public class PodcastList {

	public interface IModel {
		List<PodcastItem> retrievePodcasts() throws Exception;
		Bitmap loadPodcastImage(PodcastItem item);
	}

	public interface IView {
		void updatePodcasts(List<PodcastItem> podcasts);
		void showProgress();
		void closeProgress();
		void showErrorMessage(String errorMessage);
		void updatePodcastImage(int index);
	}

	public interface IPodcastListEngine {
		void refresh(boolean resetCache);
		void cancelUpdate();
		void detach();
		void attach(IView view);
	}
}
