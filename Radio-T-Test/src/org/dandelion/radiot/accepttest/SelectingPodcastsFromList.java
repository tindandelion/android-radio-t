package org.dandelion.radiot.accepttest;


import android.net.Uri;
import org.dandelion.radiot.accepttest.drivers.ApplicationDriver;
import org.dandelion.radiot.accepttest.drivers.PodcastListDriver;
import org.dandelion.radiot.helpers.FakeDownloadManager;
import org.dandelion.radiot.helpers.FakePodcastPlayer;
import org.dandelion.radiot.helpers.PodcastListAcceptanceTestCase;
import org.dandelion.radiot.podcasts.PodcastsApp;
import org.dandelion.radiot.podcasts.core.PodcastDownloadManager;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastPlayer;

import java.io.File;

class TestingPodcastsApp extends PodcastsApp {
    private PodcastPlayer player;
    private FakeDownloadManager downloadManager;
    public static final File PODCAST_DOWNLOAD_FOLDER = new File("/mnt/downloads");

    TestingPodcastsApp(PodcastPlayer player, FakeDownloadManager downloadManager) {
        super();
        this.player = player;
        this.downloadManager = downloadManager;
    }

    @Override
    public PodcastPlayer getPlayer() {
        return player;
    }

    @Override
    protected PodcastDownloadManager createDownloadManager() {
        return downloadManager;
    }
}

public class SelectingPodcastsFromList extends PodcastListAcceptanceTestCase {
    private FakePodcastPlayer player;
    private PodcastListDriver listDriver;
    private FakeDownloadManager downloadManager;

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
        Uri uri = item.getAudioUri();
        String basename = uri.getLastPathSegment();
        downloadManager.assertSubmittedRequest(uri,
                toDestinationUrl(TestingPodcastsApp.PODCAST_DOWNLOAD_FOLDER, basename));
    }

    private Uri toDestinationUrl(File folder, String basename) {
        return Uri.fromFile(new File(folder, basename));
    }

    private void setupEnvironment() {
        player = new FakePodcastPlayer();
        downloadManager = new FakeDownloadManager();
        PodcastsApp.setTestingInstance(new TestingPodcastsApp(player, downloadManager));
    }
}
