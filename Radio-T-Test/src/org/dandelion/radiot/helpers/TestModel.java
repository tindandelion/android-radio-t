package org.dandelion.radiot.helpers;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.dandelion.radiot.PodcastItem;
import org.dandelion.radiot.PodcastList.IModel;

import android.graphics.Bitmap;

public class TestModel implements IModel {
	
	private CountDownLatch podcastListLatch;
	private List<PodcastItem> podcastsToReturn;
	private CountDownLatch imageLatch;
	private Bitmap imageToReturn;

	public TestModel() {
		podcastListLatch = new CountDownLatch(1);
		imageLatch = new CountDownLatch(1);
	}

	public List<PodcastItem> retrievePodcasts() throws Exception {
		podcastListLatch.await();
		return podcastsToReturn;
	}

	public Bitmap loadPodcastImage(PodcastItem item) {
		try {
			imageLatch.await();
		} catch (InterruptedException e) {
		}
		return imageToReturn;
	}
	
	public void returnsPodcasts(List<PodcastItem> list) {
		podcastsToReturn = list;
		podcastListLatch.countDown();
	}
	
	public void returnsPodcastImage(Bitmap image) {
		imageToReturn = image;
		imageLatch.countDown();
	}

}
