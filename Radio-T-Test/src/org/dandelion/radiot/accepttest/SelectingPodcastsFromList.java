package org.dandelion.radiot.accepttest;


import android.content.Context;
import android.net.Uri;
import org.dandelion.radiot.accepttest.drivers.ApplicationDriver;
import org.dandelion.radiot.accepttest.drivers.PodcastListDriver;
import org.dandelion.radiot.helpers.FakeDownloadManager;
import org.dandelion.radiot.helpers.FakePodcastPlayer;
import org.dandelion.radiot.helpers.PodcastListAcceptanceTestCase;
import org.dandelion.radiot.podcasts.PodcastsApp;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastProcessor;
import org.dandelion.radiot.podcasts.download.FakeDownloaderActivity;
import org.dandelion.radiot.podcasts.download.PodcastDownloadManager;

import java.io.File;

public class SelectingPodcastsFromList extends PodcastListAcceptanceTestCase {
    public static final String SAMPLE_URL = "http://example.com/podcast_file.mp3";
    private static final File DOWNLOAD_FOLDER = new File("/mnt/downloads");

    private FakePodcastPlayer player;
    private FakeDownloadManager downloadManager;
    private TestingPodcastsApp application;
    private ApplicationDriver appDriver;

    @Override
	protected void setUp() throws Exception {
		super.setUp();
        setupEnvironment();
        appDriver = createApplicationDriver();
    }

    public void testPlayPodcastFromInternet() throws Exception {
        PodcastListDriver driver = gotoPodcastListPage();
        PodcastItem item = driver.selectItemForPlaying(0);
		player.assertIsPlaying(item.getAudioUri());
	}

    public void testDownloadPodcastLocally() throws Exception {
        PodcastListDriver driver = gotoPodcastListPage();
        driver.makeSamplePodcastWithUrl(SAMPLE_URL);

        driver.selectItemForDownloading(0);

        downloadManager.assertSubmittedRequest(
                "http://example.com/podcast_file.mp3",
                new File(DOWNLOAD_FOLDER, "podcast_file.mp3"));
    }

    public void testInformsUserOnUnsupportedPlatforms() throws Exception {
        application.setDownloadSupported(false);
        PodcastListDriver driver = gotoPodcastListPage();

        driver.selectItemForDownloading(0);

        appDriver.assertCurrentActivity(
                "Should inform user of unsupported platform",
                FakeDownloaderActivity.class);

    }

    private PodcastListDriver gotoPodcastListPage() throws InterruptedException {
        PodcastListDriver driver = appDriver.visitMainShowPage2();
        mainShowPresenter().assertPodcastListIsUpdated();
        return driver;
    }

    private void setupEnvironment() {
        player = new FakePodcastPlayer();
        downloadManager = new FakeDownloadManager();
        application = new TestingPodcastsApp(getInstrumentation().getTargetContext(),
                player, downloadManager);
        application.setDownloadFolder(DOWNLOAD_FOLDER);
        PodcastsApp.setTestingInstance(application);
    }
}

class TestingPodcastsApp extends PodcastsApp {
    private PodcastProcessor player;
    private PodcastDownloadManager downloadManager;
    private boolean downloadSupported = true;
    private File downloadFolder;

    TestingPodcastsApp(Context context, PodcastProcessor player, PodcastDownloadManager downloadManager) {
        super(context);
        this.player = player;
        this.downloadManager = downloadManager;
    }

    @Override
    public PodcastProcessor createPlayer() {
        return player;
    }

    @Override
    public PodcastDownloadManager createDownloadManager() {
        return downloadManager;
    }

    @Override
    protected File getSystemDownloadFolder() {
        return downloadFolder;
    }

    @Override
    protected boolean supportsDownload() {
        return downloadSupported;
    }

    public void setDownloadSupported(boolean value) {
        downloadSupported = value;
    }

    public void setDownloadFolder(File value) {
        downloadFolder = value;
    }
}
