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

    private Downloader manager;
    private DownloadStarter downloader;
    private DownloadFolder downloadFolder;
    private DownloadTask task;

    @Before
    public void setUp() throws Exception {
        downloadFolder = mock(DownloadFolder.class);
        manager = mock(Downloader.class);
        downloader = new DownloadStarter(manager, downloadFolder);
        task = new DownloadTask(SOURCE_URL);
    }

    @Test
    public void submitsTaskToDownloader() throws Exception {
        downloader.startDownloading(task);
        verify(manager).submit(task);
    }

    @Test
    public void fillsLocalPathForTask() throws Exception {
        File destPath = new File("/mnt/download/filename.mp3");
        when(downloadFolder.makePathForUrl(SOURCE_URL))
                .thenReturn(destPath);
        downloader.startDownloading(task);
        assertEquals(task.localPath, destPath);
    }

    @Test
    public void ensuresDownloadFolderExists() throws Exception {
        downloader.startDownloading(task);
        verify(downloadFolder).mkdirs();
    }
}
