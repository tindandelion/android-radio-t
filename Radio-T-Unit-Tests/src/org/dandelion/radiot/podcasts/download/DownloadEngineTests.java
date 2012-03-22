package org.dandelion.radiot.podcasts.download;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class DownloadEngineTests {
    public static final String SOURCE_FILENAME = "rt_podcast_1.mp3";
    public static final String SOURCE_URL = "http://radio-t.com/" + SOURCE_FILENAME;

    private DownloadManager downloadManager;
    private DownloadEngine downloader;
    private DownloadFolder downloadFolder;
    private DownloadManager.DownloadTask task;
    private MediaScanner scanner;
    private NotificationManager notificationManager;
    public static final File LOCAL_PATH = new File("/mnt/download/filename.mp3");

    @Before
    public void setUp() throws Exception {
        downloadFolder = mock(DownloadFolder.class);
        downloadManager = mock(DownloadManager.class);
        scanner = mock(MediaScanner.class);
        notificationManager = mock(NotificationManager.class);
        downloader = new DownloadEngine(downloadManager, downloadFolder, scanner, notificationManager);
        task = new DownloadManager.DownloadTask();
    }

    @Test
    public void submitsTaskToDownloader() throws Exception {
        downloader.startDownloading(task);
        verify(downloadManager).submit(task);
    }

    @Test
    public void constructsLocalPathForTaskFromSourceUrl() throws Exception {
        when(downloadFolder.makePathForUrl(SOURCE_URL))
                .thenReturn(LOCAL_PATH);

        task.url = SOURCE_URL;
        downloader.startDownloading(task);

        assertEquals(task.localPath, LOCAL_PATH);
    }

    @Test
    public void ensuresDownloadFolderExists() throws Exception {
        downloader.startDownloading(task);
        verify(downloadFolder).mkdirs();
    }

    @Test
    public void scansAudioFileWhenDownloadComplete() throws Exception {
        task.isSuccessful = true;
        task.localPath = LOCAL_PATH;
        when(downloadManager.query(1)).thenReturn(task);
        downloader.finishDownload(1);
        verify(scanner).scanAudioFile(LOCAL_PATH);
    }

    @Test
    public void showsNotificationIconOnCompletion() throws Exception {
        task.isSuccessful = true;
        task.title = "Podcast 1";
        task.localPath = LOCAL_PATH;
        when(downloadManager.query(1)).thenReturn(task);
        downloader.finishDownload(1);
        verify(notificationManager).showNotification(task.title, LOCAL_PATH);
    }

    @Test
    public void skipsCancelledTasks() throws Exception {
        when(downloadManager.query(1)).thenReturn(null);
        downloader.finishDownload(1);
        verifyZeroInteractions(scanner);
    }

    @Test
    public void skipsFailedTasks() throws Exception {
        task.isSuccessful = false;
        when(downloadManager.query(1)).thenReturn(task);
        downloader.finishDownload(1);
        verifyZeroInteractions(scanner);
    }
}
