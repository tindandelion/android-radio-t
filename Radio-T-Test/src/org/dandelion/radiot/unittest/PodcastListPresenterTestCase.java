package org.dandelion.radiot.unittest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import org.dandelion.radiot.PodcastItem;
import org.dandelion.radiot.PodcastList.IPresenter;
import org.dandelion.radiot.PodcastList.IView;
import org.dandelion.radiot.PodcastListPresenter;
import org.dandelion.radiot.helpers.TestModel;

import android.graphics.Bitmap;
import android.os.Looper;

public class PodcastListPresenterTestCase extends TestCase {
	private IPresenter presenter;
	protected List<PodcastItem> publishedPodcasts;
	private CountDownLatch podcastListPublishedLatch;
	private CountDownLatch updateFinishedLatch;
	private int updatedPodcastImage;
	private TestModel model;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		podcastListPublishedLatch = new CountDownLatch(1);
		updateFinishedLatch = new CountDownLatch(1);
		updatedPodcastImage = -1;
		model = new TestModel();
		presenter = newPresenter();
	}

	public void testRetrieveAndPublishPodcastList() throws Exception {
		ArrayList<PodcastItem> podcastList = newPodcastList();
		
		startPodcastListUpdate();
		model.returnsPodcasts(podcastList);
		waitUntilPodcastListPublished();
		
		assertPublishedPodcasts(podcastList);
	}
	
	public void testRetrievePodcastImagesAfterPublishingList() throws Exception {
		ArrayList<PodcastItem> podcastList = newPodcastList();
		PodcastItem item = new PodcastItem();
		podcastList.add(item);
		
		model.returnsPodcasts(podcastList);
		startPodcastListUpdate();
		waitUntilPodcastListPublished();
		assertNull(item.getImage());
		
		Bitmap image = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);
		model.returnsPodcastImage(image);
		waitUntilAllUpdateIsFinsihed();
		assertEquals(image, item.getImage());
	}
	
	public void testUpdatingViewWhenPodcastImageIsLoaded() throws Exception {
		ArrayList<PodcastItem> podcastList = newPodcastList();
		PodcastItem item = new PodcastItem();
		podcastList.add(item);
		
		model.returnsPodcasts(podcastList);
		startPodcastListUpdate();
		waitUntilPodcastListPublished();
		assertNull(item.getImage());
		
		Bitmap image = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);
		model.returnsPodcastImage(image);
		waitUntilAllUpdateIsFinsihed();
		assertPodcastImageUpdated(0);
	}
	
	private void assertPodcastImageUpdated(int i) {
		assertEquals(i, updatedPodcastImage);
	}

	private void waitUntilAllUpdateIsFinsihed() throws InterruptedException {
		if (!updateFinishedLatch.await(60, TimeUnit.SECONDS)) {
			fail("Failed to wait until all update is finished");
		}
	}

	protected ArrayList<PodcastItem> newPodcastList() {
		return new ArrayList<PodcastItem>();
	}

	private void assertPublishedPodcasts(ArrayList<PodcastItem> podcastList) {
		assertEquals(podcastList, publishedPodcasts);
	}

	private void waitUntilPodcastListPublished() throws InterruptedException {
		if (!podcastListPublishedLatch.await(60, TimeUnit.SECONDS)) { 
			fail("Failed to wait until podcast list is published");
		}
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

	private IPresenter newPresenter() {
		PodcastListPresenter p = new PodcastListPresenter(model) {
			@Override
			public void taskFinished() {
				super.taskFinished();
				updateFinishedLatch.countDown();
			}
		};
		p.attach(newView());
		return p;
	}

	private IView newView() {
		return new IView() {
			public void updatePodcasts(List<PodcastItem> podcasts) {
				publishedPodcasts = podcasts;
				podcastListPublishedLatch.countDown();
			}
			
			public void showProgress() {
			}
			
			public void showErrorMessage(String errorMessage) {
			}
			
			public void closeProgress() {
			}
			
			public void updatePodcastImage(int index) {
				updatedPodcastImage = index;
			}
		};
	}
}
