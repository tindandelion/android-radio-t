package org.dandelion.radiot.podcasts.download;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DownloadEngineTest {
    public static final String SOURCE_FILENAME = "rt_podcast_1.mp3";
    public static final String SOURCE_URL = "http://radio-t.com/" + SOURCE_FILENAME;
    public static final File LOCAL_PATH = new File("/mnt/download/filename.mp3");

    private DownloadManager downloadManager;
    private DownloadEngine downloader;
    private DownloadFolder downloadFolder;
    private DownloadManager.DownloadTask task;
    private DownloadProcessor processor;

    @Before
    public void setUp() throws Exception {
        downloadFolder = mock(DownloadFolder.class);
        downloadManager = mock(DownloadManager.class);
        processor = mock(DownloadProcessor.class);
        downloader = new DownloadEngine(downloadManager, downloadFolder, processor);
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
    public void signalsCompletionToPostProcessor() throws Exception {
        task.isSuccessful = true;
        task.localPath = LOCAL_PATH;
        when(downloadManager.query(1)).thenReturn(task);
        downloader.finishDownload(1);
        verify(processor).downloadComplete(task.title, LOCAL_PATH);
    }

    @Test
    public void signalsErrorToPostProcessor() throws Exception {
        task.isSuccessful = false;
        task.errorCode = 1006;
        task.title = "Podcast 1";
        when(downloadManager.query(1)).thenReturn(task);
        downloader.finishDownload(1);
        verify(processor).downloadError(task.title, task.errorCode);
    }

    @Test
    public void skipsCancelledTasks() throws Exception {
        when(downloadManager.query(1)).thenReturn(null);
        downloader.finishDownload(1);
        verifyZeroInteractions(processor);
    }
}
