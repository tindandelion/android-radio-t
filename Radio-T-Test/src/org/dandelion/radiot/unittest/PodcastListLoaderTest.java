package org.dandelion.radiot.unittest;

import android.os.Looper;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.dandelion.radiot.helpers.FakeCache;
import org.dandelion.radiot.podcasts.core.*;

import java.util.concurrent.LinkedBlockingQueue;

public class PodcastListLoaderTest extends TestCase {
	private TestPodcastsProvider podcasts;
	private TestView view;
	private PodcastListLoader loader;

    public void testRetrieveAndPublishPodcastList() throws Exception {
        PodcastList podcastList = new PodcastList();

		startPodcastListUpdate();
		podcasts.returnsPodcasts(podcastList);
		
		view.waitAndCheckUpdatedPodcasts(podcastList);
	}

    @Override
	protected void setUp() throws Exception {
		super.setUp();
		podcasts = new TestPodcastsProvider();
        view = new TestView();
		loader = newLoader(podcasts);
	}

	private PodcastListLoader newLoader(PodcastsProvider podcasts) {
		AsyncPodcastListLoader p = new AsyncPodcastListLoader(podcasts, new FakeCache());
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
    public PodcastList retrieve() throws Exception {
        return podcastQueue.take();
    }

    public void returnsPodcasts(PodcastList pl) {
        podcastQueue.add(pl);
    }
}

class TestView implements ProgressListener, PodcastsConsumer {
	private LinkedBlockingQueue<PodcastList> updatedPodcasts =
            new LinkedBlockingQueue<PodcastList>();

	public void onFinished() {
	}

	public void waitAndCheckUpdatedPodcasts(PodcastList pl) throws InterruptedException {
		Assert.assertEquals(pl, updatedPodcasts.take());
	}

	public void onError(String errorMessage) {
	}

	public void onStarted() {
	}

	public void updatePodcasts(PodcastList podcasts) {
		updatedPodcasts.add(podcasts);
	}
}
