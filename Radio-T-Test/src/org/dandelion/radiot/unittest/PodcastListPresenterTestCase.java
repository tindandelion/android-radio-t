package org.dandelion.radiot.unittest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.dandelion.radiot.PodcastItem;
import org.dandelion.radiot.PodcastList.IPresenter;
import org.dandelion.radiot.PodcastList.IView;
import org.dandelion.radiot.PodcastListPresenter;
import org.dandelion.radiot.helpers.TestModel;

import android.graphics.Bitmap;
import android.os.Looper;

public class PodcastListPresenterTestCase extends TestCase {

	private TestModel model;
	private TestView view;
	private IPresenter presenter;

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
		assertNull(item.getImage());

		Bitmap image = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);
		model.returnsPodcastImage(image);
		view.assertPodcastImageUpdated(0);
		assertEquals(image, item.getImage());
	}

	protected ArrayList<PodcastItem> newPodcastList() {
		return new ArrayList<PodcastItem>();
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		model = new TestModel();
		view = new TestView();
		presenter = newPresenter();
	}

	private IPresenter newPresenter() {
		PodcastListPresenter p = new PodcastListPresenter(model);
		p.attach(view);
		return p;
	}

	private void startPodcastListUpdate() {
		new Thread(new Runnable() {

			public void run() {
				Looper.prepare();
				presenter.refresh(false);
				Looper.loop();
			}
		}).start();
	}
}

class TestView implements IView {
	private LinkedBlockingQueue<Integer> updatedImages; 
	private LinkedBlockingQueue<List<PodcastItem>> updatedPodcasts;
	
	public TestView() {
		updatedPodcasts = new LinkedBlockingQueue<List<PodcastItem>>();
		updatedImages = new LinkedBlockingQueue<Integer>();
	}
	
	public void assertPodcastImageUpdated(int index) throws InterruptedException {
		Assert.assertEquals(new Integer(index), updatedImages.take());
	}

	public void closeProgress() {
	}

	public void waitAndCheckUpdatedPodcasts(ArrayList<PodcastItem> podcastList) throws InterruptedException {
		Assert.assertEquals(podcastList, updatedPodcasts.take());
	}

	public void waitUntilPodcastListUpdated() throws InterruptedException {
		updatedPodcasts.take();
	}

	public void showErrorMessage(String errorMessage) {
	}

	public void showProgress() {
	}

	public void updatePodcastImage(int index) {
		try {
			updatedImages.put(new Integer(index));
		} catch (InterruptedException e) {
		}
	}

	public void updatePodcasts(List<PodcastItem> podcasts) {
		updatedPodcasts.add(podcasts);
	}
}
