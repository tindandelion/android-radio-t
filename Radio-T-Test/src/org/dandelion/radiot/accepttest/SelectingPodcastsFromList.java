package org.dandelion.radiot.accepttest;


import org.dandelion.radiot.accepttest.drivers.ApplicationDriver;
import org.dandelion.radiot.accepttest.drivers.PodcastListDriver;
import org.dandelion.radiot.helpers.FakePodcastDownloader;
import org.dandelion.radiot.helpers.FakePodcastPlayer;
import org.dandelion.radiot.helpers.PodcastListAcceptanceTestCase;
import org.dandelion.radiot.podcasts.PodcastsApp;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastPlayer;

class TestingPodcastsApp extends PodcastsApp {
    private PodcastPlayer player;
    private FakePodcastDownloader downloader;

    TestingPodcastsApp(PodcastPlayer player, FakePodcastDownloader downloader) {
        super();
        this.player = player;
        this.downloader = downloader;
    }

    @Override
    public PodcastPlayer getPodcastPlayer() {
        return player;
    }
}

public class SelectingPodcastsFromList extends PodcastListAcceptanceTestCase {
    private FakePodcastPlayer player;
    private PodcastListDriver listDriver;
    private FakePodcastDownloader downloader;

    @Override
	protected void setUp() throws Exception {
		super.setUp();
        setupEnvironment();
        ApplicationDriver appDriver = createApplicationDriver();
		listDriver = appDriver.visitMainShowPage2();
		mainShowPresenter().assertPodcastListIsUpdated();
	}

    public void testSelectPodcastForPlaying() throws Exception {
        PodcastItem item = listDriver.selectItemForPlaying(0);
		player.assertIsPlaying(item.getAudioUri());
	}

    public void testStartDownloadingPodcast() throws Exception {
        PodcastItem item = listDriver.selectItemForDownloading(0);
        downloader.assertIsDownloading(item);
    }

    private void setupEnvironment() {
        player = new FakePodcastPlayer();
        downloader = new FakePodcastDownloader();
        PodcastsApp.setTestingInstance(new TestingPodcastsApp(player, downloader));
    }
}
