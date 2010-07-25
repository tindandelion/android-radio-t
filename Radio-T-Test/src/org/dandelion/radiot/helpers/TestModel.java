package org.dandelion.radiot.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.dandelion.radiot.PodcastItem;
import org.dandelion.radiot.PodcastList.IModel;

import android.graphics.Bitmap;

public class TestModel implements IModel {

	private LinkedBlockingQueue<List<PodcastItem>> podcastQueue;
	private LinkedBlockingQueue<Bitmap> imageQueue;

	public TestModel() {
		podcastQueue = new LinkedBlockingQueue<List<PodcastItem>>();
		imageQueue = new LinkedBlockingQueue<Bitmap>();
	}

	public List<PodcastItem> retrievePodcasts() throws Exception {
		return podcastQueue.take();
	}

	public Bitmap loadPodcastImage(PodcastItem item) {
		try {
			return imageQueue.take();
		} catch (InterruptedException e) {
			return null;
		}
	}

	public void returnsPodcasts(List<PodcastItem> list) {
		podcastQueue.add(list);
	}

	public void returnsEmptyPodcastList() {
		returnsPodcasts(new ArrayList<PodcastItem>());
	}

	public void returnsPodcastImage(Bitmap image) {
		imageQueue.add(image);
	}

}
