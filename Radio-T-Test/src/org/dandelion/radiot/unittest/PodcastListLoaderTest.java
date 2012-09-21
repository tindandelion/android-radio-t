package org.dandelion.radiot.unittest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.dandelion.radiot.podcasts.core.*;
import org.dandelion.radiot.podcasts.core.PodcastListLoader;
import org.dandelion.radiot.helpers.TestModel;

import android.graphics.Bitmap;
import android.os.Looper;

public class PodcastListLoaderTest extends TestCase {
	private TestModel model;
	private TestView view;
	private PodcastListLoader loader;

	public void testRetrieveAndPublishPodcastList() throws Exception {
		ArrayList<PodcastItem> podcastList = newPodcastList();

		startPodcastListUpdate();
		model.returnsPodcasts(podcastList);
		
		view.waitAndCheckUpdatedPodcasts(podcastList);
	}
	
	public void testLoadingPodcastImages() throws Exception {
		ArrayList<PodcastItem> podcastList = newPodcastList();
		PodcastItem item = new PodcastItem();
		podcastList.add(item);

		model.returnsPodcasts(podcastList);
		startPodcastListUpdate();
		view.waitUntilPodcastListUpdated();
		assertNull(item.getThumbnail());

		Bitmap image = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);
		model.returnsPodcastImage(image);
		view.assertPodcastImageUpdated(0);
		assertEquals(image, item.getThumbnail());
	}

	protected ArrayList<PodcastItem> newPodcastList() {
		return new ArrayList<PodcastItem>();
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		model = new TestModel();
		view = new TestView();
		loader = newPresenter();
	}

	private PodcastListLoader newPresenter() {
        // TODO: Here will be the change soon
        NullThumbnailProvider thumbnails = new NullThumbnailProvider();
		AsyncPodcastListLoader p = new AsyncPodcastListLoader(model, thumbnails);
		p.attach(view, view);
		return p;
	}

	private void startPodcastListUpdate() {
		new Thread(new Runnable() {

			public void run() {
				Looper.prepare();
				loader.refresh(false);
				Looper.loop();
			}
		}).start();
	}
}

class TestView implements ProgressListener, PodcastListConsumer {
	private LinkedBlockingQueue<Integer> updatedImages; 
	private LinkedBlockingQueue<List<PodcastItem>> updatedPodcasts;
	
	public TestView() {
		updatedPodcasts = new LinkedBlockingQueue<List<PodcastItem>>();
		updatedImages = new LinkedBlockingQueue<Integer>();
	}
	
	public void assertPodcastImageUpdated(int index) throws InterruptedException {
		Assert.assertEquals(new Integer(index), updatedImages.take());
	}

	public void onFinished() {
	}

	public void waitAndCheckUpdatedPodcasts(ArrayList<PodcastItem> podcastList) throws InterruptedException {
		Assert.assertEquals(podcastList, updatedPodcasts.take());
	}

	public void waitUntilPodcastListUpdated() throws InterruptedException {
		updatedPodcasts.take();
	}

	public void onError(String errorMessage) {
	}

	public void onStarted() {
	}

	public void updateThumbnail(int index) {
		try {
			updatedImages.put(index);
		} catch (InterruptedException ignored) {
		}
	}

	public void updatePodcasts(List<PodcastItem> podcasts) {
		updatedPodcasts.add(podcasts);
	}
}
