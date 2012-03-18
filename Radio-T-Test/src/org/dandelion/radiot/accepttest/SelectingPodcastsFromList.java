package org.dandelion.radiot.accepttest;


import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import org.dandelion.radiot.accepttest.drivers.ApplicationDriver;
import org.dandelion.radiot.accepttest.drivers.PodcastListDriver;
import org.dandelion.radiot.helpers.FakeDownloadManager;
import org.dandelion.radiot.helpers.FakeMediaScanner;
import org.dandelion.radiot.helpers.FakePodcastPlayer;
import org.dandelion.radiot.helpers.PodcastListAcceptanceTestCase;
import org.dandelion.radiot.podcasts.PodcastsApp;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastProcessor;
import org.dandelion.radiot.podcasts.download.FakeDownloaderActivity;
import org.dandelion.radiot.podcasts.download.Downloader;
import org.dandelion.radiot.podcasts.download.MediaScanner;

import java.io.File;
import java.io.IOException;

public class SelectingPodcastsFromList extends PodcastListAcceptanceTestCase {

    private static File getDownloadFolder() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    }

    public static final String SAMPLE_URL = "http://example.com/podcast_file.mp3";
    private static final File DOWNLOAD_FOLDER = getDownloadFolder();

    private FakePodcastPlayer player;
    private FakeDownloadManager downloadManager;
    private TestingPodcastsApp application;
    private ApplicationDriver appDriver;
    private FakeMediaScanner mediaScanner;

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

    public void testDownloadPodcastFileLocally() throws Exception {
        PodcastListDriver driver = gotoPodcastListPage();
        driver.makeSamplePodcastWithUrl(SAMPLE_URL);
        File localPath = new File(DOWNLOAD_FOLDER, "podcast_file.mp3");

        driver.selectItemForDownloading(0);
        downloadManager.assertSubmittedRequest(SAMPLE_URL, localPath);

        application.signalDownloadComplete(localPath);
        mediaScanner.assertScannedFile(localPath);
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
        mediaScanner = new FakeMediaScanner();
        application = new TestingPodcastsApp(getInstrumentation().getTargetContext(),
                player, downloadManager, mediaScanner);
        application.setDownloadFolder(DOWNLOAD_FOLDER);
        PodcastsApp.setTestingInstance(application);
    }
}

class TestingPodcastsApp extends PodcastsApp {
    private PodcastProcessor player;
    private Downloader downloadManager;
    private boolean downloadSupported = true;
    private File downloadFolder;
    private MediaScanner mediaScanner;

    TestingPodcastsApp(Context context, PodcastProcessor player, Downloader downloadManager, MediaScanner scanner) {
        super(context);
        this.player = player;
        this.downloadManager = downloadManager;
        this.mediaScanner = scanner;
    }

    @Override
    public PodcastProcessor createPlayer() {
        return player;
    }

    @Override
    public Downloader createDownloadManager() {
        return downloadManager;
    }

    @Override
    protected File getSystemDownloadFolder() {
        return downloadFolder;
    }

    @Override
    public MediaScanner createMediaScanner() {
        return mediaScanner;
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

    public void signalDownloadComplete(File localPath) {
        createLocalFile(localPath);
        sendCompletionBroadcast();
    }

    private void createLocalFile(File localPath) {
        try {
            localPath.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendCompletionBroadcast() {
        Intent intent = new Intent(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        intent.putExtra(DownloadManager.EXTRA_DOWNLOAD_ID, FakeDownloadManager.TASK_ID);
        context.sendBroadcast(intent);
    }
}
