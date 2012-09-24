package org.dandelion.radiot.unittest;

import android.graphics.Bitmap;
import android.os.Looper;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.dandelion.radiot.podcasts.core.*;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class PodcastListLoaderTest extends TestCase {
	private TestPodcastsProvider podcasts;
	private TestView view;
	private PodcastListLoader loader;
    private TestThumbnailProvider thumbnails;

    public void testRetrieveAndPublishPodcastList() throws Exception {
        PodcastList podcastList = new PodcastList();

		startPodcastListUpdate();
		podcasts.returnsPodcasts(podcastList);
		
		view.waitAndCheckUpdatedPodcasts(podcastList);
	}
	
	public void testLoadingPodcastImages() throws Exception {
        PodcastList list = new PodcastList();
		PodcastItem item = new PodcastItem();
		list.add(item);

		podcasts.returnsPodcasts(list);
        Bitmap image = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);
        thumbnails.returnsPodcastImage(image);

        startPodcastListUpdate();
		view.waitUntilPodcastListUpdated();
		assertEquals(image, item.getThumbnail());
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
    private LinkedBlockingQueue<PodcastList> podcastQueue =
            new LinkedBlockingQueue<PodcastList>();

    @Override
    public List<PodcastItem> retrieveAll() throws Exception {
        return podcastQueue.take();
    }

    public void returnsPodcasts(PodcastList pl) {
        podcastQueue.add(pl);
    }
}

class TestThumbnailProvider implements ThumbnailProvider {
    private Bitmap image;

    @Override
    public Bitmap thumbnailFor(PodcastItem item) {
        return image;
    }

    @Override
    public byte[] thumbnailDataFor(PodcastItem item) {
        return null;
    }

    public void returnsPodcastImage(Bitmap image) {
        this.image = image;
    }
}

class TestView implements ProgressListener, PodcastListConsumer {
	private LinkedBlockingQueue<PodcastList> updatedPodcasts =
            new LinkedBlockingQueue<PodcastList>();

	public void onFinished() {
	}

	public void waitAndCheckUpdatedPodcasts(PodcastList pl) throws InterruptedException {
		Assert.assertEquals(pl, updatedPodcasts.take());
	}

	public void waitUntilPodcastListUpdated() throws InterruptedException {
		updatedPodcasts.take();
	}

	public void onError(String errorMessage) {
	}

	public void onStarted() {
	}

	public void updatePodcasts(List<PodcastItem> podcasts) {
		updatedPodcasts.add(new PodcastList(podcasts));
	}
}
