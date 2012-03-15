package org.dandelion.radiot.accepttest;


import android.content.Context;
import android.net.Uri;
import org.dandelion.radiot.accepttest.drivers.ApplicationDriver;
import org.dandelion.radiot.accepttest.drivers.PodcastListDriver;
import org.dandelion.radiot.helpers.FakeDownloadManager;
import org.dandelion.radiot.helpers.FakePodcastPlayer;
import org.dandelion.radiot.helpers.PodcastListAcceptanceTestCase;
import org.dandelion.radiot.podcasts.PodcastsApp;
import org.dandelion.radiot.podcasts.download.PodcastDownloadManager;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastPlayer;
import org.dandelion.radiot.podcasts.download.FakeDownloaderActivity;

import java.io.File;

public class SelectingPodcastsFromList extends PodcastListAcceptanceTestCase {
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

    // TODO: Things may be more expressive if using predefined podcast item
    public void testDownloadPodcastLocally() throws Exception {
        PodcastListDriver driver = gotoPodcastListPage();
        PodcastItem item = driver.selectItemForDownloading(0);

        String src = item.getAudioUri();
        String basename = Uri.parse(src).getLastPathSegment();
        downloadManager.assertSubmittedRequest(src,
                toLocalFile(TestingPodcastsApp.PODCAST_DOWNLOAD_FOLDER, basename));
    }

    public void testInformsUserOnUnsupportedPlatforms() throws Exception {
        application.setDownloadSupported(false);
        PodcastListDriver driver = gotoPodcastListPage();
        driver.selectItemForDownloading(0);

        appDriver.assertCurrentActivity("Should inform user of unsupported platform",
                FakeDownloaderActivity.class);

    }

    private PodcastListDriver gotoPodcastListPage() throws InterruptedException {
        PodcastListDriver driver = appDriver.visitMainShowPage2();
        mainShowPresenter().assertPodcastListIsUpdated();
        return driver;
    }

    private File toLocalFile(File folder, String basename) {
        return new File(folder, basename);
    }

    private void setupEnvironment() {
        player = new FakePodcastPlayer();
        downloadManager = new FakeDownloadManager();
        application = new TestingPodcastsApp(getInstrumentation().getTargetContext(),
                player, downloadManager);
        PodcastsApp.setTestingInstance(application);
    }
}

class TestingPodcastsApp extends PodcastsApp {
    private PodcastPlayer player;
    private FakeDownloadManager downloadManager;
    public static final File PODCAST_DOWNLOAD_FOLDER = new File("/mnt/downloads");
    private boolean downloadSupported = true;

    TestingPodcastsApp(Context context, PodcastPlayer player, FakeDownloadManager downloadManager) {
        super(context);
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

    @Override
    protected File getSystemDownloadFolder() {
        return PODCAST_DOWNLOAD_FOLDER;
    }

    @Override
    protected boolean supportsDownload() {
        return downloadSupported;
    }

    public void setDownloadSupported(boolean value) {
        downloadSupported = value;
    }
}
