package org.dandelion.radiot.unittest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.dandelion.radiot.podcasts.core.*;
import org.dandelion.radiot.podcasts.core.PodcastListLoader;

import android.graphics.Bitmap;
import android.os.Looper;

public class PodcastListLoaderTest extends TestCase {
	private TestPodcastsProvider podcasts;
	private TestView view;
	private PodcastListLoader loader;
    private TestThumbnailProvider thumbnails;

    public void testRetrieveAndPublishPodcastList() throws Exception {
		ArrayList<PodcastItem> podcastList = newPodcastList();

		startPodcastListUpdate();
		podcasts.returnsPodcasts(podcastList);
		
		view.waitAndCheckUpdatedPodcasts(podcastList);
	}
	
	public void testLoadingPodcastImages() throws Exception {
		ArrayList<PodcastItem> podcastList = newPodcastList();
		PodcastItem item = new PodcastItem();
		podcastList.add(item);

		podcasts.returnsPodcasts(podcastList);
		startPodcastListUpdate();
		view.waitUntilPodcastListUpdated();
		assertNull(item.getThumbnail());

		Bitmap image = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);
		thumbnails.returnsPodcastImage(image);
		view.assertPodcastImageUpdated(0);
		assertEquals(image, item.getThumbnail());
	}

	protected ArrayList<PodcastItem> newPodcastList() {
		return new ArrayList<PodcastItem>();
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		podcasts = new TestPodcastsProvider();
        thumbnails = new TestThumbnailProvider();
		view = new TestView();
		loader = newLoader(podcasts, thumbnails);
	}

	private PodcastListLoader newLoader(PodcastsProvider podcasts, ThumbnailProvider thumbnails) {
		AsyncPodcastListLoader p = new AsyncPodcastListLoader(podcasts, thumbnails);
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

class TestPodcastsProvider implements PodcastsProvider {
    private LinkedBlockingQueue<List<PodcastItem>> podcastQueue =
            new LinkedBlockingQueue<List<PodcastItem>>();

    @Override
    public List<PodcastItem> retrieveAll() throws Exception {
        return podcastQueue.take();
    }

    public void returnsPodcasts(List<PodcastItem> list) {
        podcastQueue.add(list);
    }
}

class TestThumbnailProvider implements ThumbnailProvider {
    private final BlockingQueue<Bitmap> imageQueue = new LinkedBlockingQueue<Bitmap>();

    @Override
    public Bitmap thumbnailFor(PodcastItem item) {
        try {
            return imageQueue.take();
        } catch (InterruptedException e) {
            return null;
        }
    }

    public void returnsPodcastImage(Bitmap image) {
        imageQueue.add(image);
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
