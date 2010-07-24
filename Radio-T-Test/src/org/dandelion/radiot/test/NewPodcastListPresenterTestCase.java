package org.dandelion.radiot.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import org.dandelion.radiot.PodcastItem;
import org.dandelion.radiot.PodcastList.IModel;
import org.dandelion.radiot.PodcastList.IPresenter;
import org.dandelion.radiot.PodcastList.IView;
import org.dandelion.radiot.PodcastListPresenter;

import android.os.Looper;

public class NewPodcastListPresenterTestCase extends TestCase {
	private IPresenter presenter;
	protected List<PodcastItem> publishedPodcasts;
	private CountDownLatch updateFinishedLatch;
	private ArrayList<PodcastItem> podcastListToPublish;
	private CountDownLatch modelPodcastRetrievalLatch;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		updateFinishedLatch = new CountDownLatch(1);
		modelPodcastRetrievalLatch = new CountDownLatch(1);
	}

	public void testRetrieveAndPublishPodcastList() throws Exception {
		ArrayList<PodcastItem> podcastList = new ArrayList<PodcastItem>();
		presenter = newPresenter();
		
		triggerPodcastListUpdate(presenter);
		modelReturnsPodcastList(podcastList);
		waitUntilPodcastListUpdateFinished();
		
		assertPublishedPodcasts(podcastList);
	}

	private void assertPublishedPodcasts(ArrayList<PodcastItem> podcastList) {
		assertEquals(podcastList, publishedPodcasts);
	}

	private void waitUntilPodcastListUpdateFinished() throws InterruptedException {
		if (!updateFinishedLatch.await(10, TimeUnit.SECONDS)) { 
			fail("Failed to wait until update is finished");
		}
	}

	private void modelReturnsPodcastList(ArrayList<PodcastItem> list) throws InterruptedException {
		podcastListToPublish = list;
		modelPodcastRetrievalLatch.countDown();
	}

	private void triggerPodcastListUpdate(final IPresenter p) {
		new Thread(new Runnable() {
			
			public void run() {
				Looper.prepare();
				p.refreshData(false);
				Looper.loop();
			}
		}).start();
	}

	private IPresenter newPresenter() {
		PodcastListPresenter p = new PodcastListPresenter(newModel()) {
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
			}
			
			public void showProgress() {
			}
			
			public void showErrorMessage(String errorMessage) {
			}
			
			public void closeProgress() {
			}
		};
	}

	private IModel newModel() {
		return new IModel() {
			
			public List<PodcastItem> retrievePodcasts() throws Exception {
				modelPodcastRetrievalLatch.await();
				return podcastListToPublish;
			}
			
			public void loadPodcastImage(PodcastItem item) {
			}
		};
	}
}
