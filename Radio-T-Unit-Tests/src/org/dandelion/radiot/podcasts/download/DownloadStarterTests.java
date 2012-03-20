package org.dandelion.radiot.podcasts.download;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class DownloadStarterTests {
    public static final String SOURCE_FILENAME = "rt_podcast_1.mp3";
    public static final String SOURCE_URL = "http://radio-t.com/" + SOURCE_FILENAME;

    private DownloadManager manager;
    private DownloadEngine downloader;
    private DownloadFolder downloadFolder;
    private DownloadManager.DownloadTask task;
    private MediaScanner scanner;

    @Before
    public void setUp() throws Exception {
        downloadFolder = mock(DownloadFolder.class);
        manager = mock(DownloadManager.class);
        scanner = mock(MediaScanner.class);
        downloader = new DownloadEngine(manager, downloadFolder, scanner);
        task = new DownloadManager.DownloadTask();
    }

    @Test
    public void submitsTaskToDownloader() throws Exception {
        downloader.startDownloading(task);
        verify(manager).submit(task);
    }

    @Test
    public void constructsLocalPathForTaskFromSourceUrl() throws Exception {
        File destPath = new File("/mnt/download/filename.mp3");
        when(downloadFolder.makePathForUrl(SOURCE_URL))
                .thenReturn(destPath);

        task.url = SOURCE_URL;
        downloader.startDownloading(task);

        assertEquals(task.localPath, destPath);
    }

    @Test
    public void ensuresDownloadFolderExists() throws Exception {
        downloader.startDownloading(task);
        verify(downloadFolder).mkdirs();
    }

    @Test
    public void scansAudioFileWhenDownloadComplete() throws Exception {
        task.isSuccessful = true;
        when(manager.query(1)).thenReturn(task);
        downloader.finishDownload(1);
        verify(scanner).scanAudioFile(task.localPath);
    }

    @Test
    public void skipsCancelledTasks() throws Exception {
        when(manager.query(1)).thenReturn(null);
        downloader.finishDownload(1);
        verifyZeroInteractions(scanner);
    }

    @Test
    public void skipsFailedTasks() throws Exception {
        task.isSuccessful = false;
        when(manager.query(1)).thenReturn(task);
        downloader.finishDownload(1);
        verifyZeroInteractions(scanner);
    }
}
