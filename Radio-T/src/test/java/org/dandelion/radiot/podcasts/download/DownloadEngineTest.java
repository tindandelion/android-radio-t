package org.dandelion.radiot.podcasts.download;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DownloadEngineTest {
    private static final String TITLE = "Podcast";
    public static final String SOURCE_FILENAME = "rt_podcast_1.mp3";
    public static final String SOURCE_URL = "http://radio-t.com/" + SOURCE_FILENAME;
    public static final File LOCAL_PATH = new File("/mnt/download/filename.mp3");

    private final DownloadFolder downloadFolder = mock(DownloadFolder.class);
    private final DownloadManager downloadManager = mock(DownloadManager.class);
    private final DownloadProcessor processor = mock(DownloadProcessor.class);
    private final DownloadManager.Request request = new DownloadManager.Request(SOURCE_URL, TITLE);
    private final DownloadEngine downloader = new DownloadEngine(downloadManager, downloadFolder, processor);

    @Test
    public void submitsTaskToDownloader() throws Exception {
        downloader.startDownloading(request);
        verify(downloadManager).submit(request);
    }

    @Test
    public void constructsLocalPathForTaskFromSourceUrl() throws Exception {
        when(downloadFolder.makePathForUrl(SOURCE_URL))
                .thenReturn(LOCAL_PATH);
        downloader.startDownloading(request);
        assertEquals(request.localPath, LOCAL_PATH);
    }

    @Test
    public void ensuresDownloadFolderExists() throws Exception {
        downloader.startDownloading(request);
        verify(downloadFolder).mkdirs();
    }

    @Test
    public void signalsCompletionToPostProcessor() throws Exception {
        DownloadManager.CompletionInfo info = DownloadManager.CompletionInfo.success(TITLE, LOCAL_PATH);
        when(downloadManager.query(1)).thenReturn(info);
        downloader.finishDownload(1);
        verify(processor).downloadComplete(TITLE, LOCAL_PATH);
    }

    @Test
    public void signalsErrorToPostProcessor() throws Exception {
        final int errorCode = 1006;
        DownloadManager.CompletionInfo info = DownloadManager.CompletionInfo.failure(TITLE, errorCode);
        when(downloadManager.query(1)).thenReturn(info);
        downloader.finishDownload(1);
        verify(processor).downloadError(TITLE, errorCode);
    }

    @Test
    public void skipsCancelledTasks() throws Exception {
        when(downloadManager.query(1)).thenReturn(null);
        downloader.finishDownload(1);
        verifyZeroInteractions(processor);
    }
}
