package org.dandelion.radiot.helpers;

import android.graphics.Bitmap;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastsProvider;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class TestModel implements PodcastsProvider {

	private LinkedBlockingQueue<List<PodcastItem>> podcastQueue;
	private LinkedBlockingQueue<Bitmap> imageQueue;

	public TestModel() {
		podcastQueue = new LinkedBlockingQueue<List<PodcastItem>>();
		imageQueue = new LinkedBlockingQueue<Bitmap>();
	}

	public List<PodcastItem> retrieveAll() throws Exception {
		return podcastQueue.take();
	}

	public Bitmap thumbnailFor(PodcastItem item) {
		try {
			return imageQueue.take();
		} catch (InterruptedException e) {
			return null;
		}
	}

	public void returnsPodcasts(List<PodcastItem> list) {
		podcastQueue.add(list);
	}

    public void returnsPodcastImage(Bitmap image) {
		imageQueue.add(image);
	}

}
