package org.dandelion.radiot.podcasts.core;

import android.graphics.Bitmap;
import java.util.List;

public class PodcastList {
	public interface IModel {
		List<PodcastItem> retrievePodcasts() throws Exception;
		Bitmap loadPodcastImage(PodcastItem item);
	}

    public interface IView extends ErrorDisplay {
		void showProgress();
		void closeProgress();
	}

	public interface IPodcastListEngine {
		void refresh(boolean resetCache);
		void cancelUpdate();
		void detach();
		void attach(IView view, PodcastListConsumer consumer);
	}

    public interface ThumbnailDownloader {
        Bitmap loadPodcastImage(String url);
    }
}
