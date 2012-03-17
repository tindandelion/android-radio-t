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
    private DownloadProcessor nextProcessor;
    private DownloadTask task;

    @Before
    public void setUp() throws Exception {
        downloadFolder = mock(DownloadFolder.class);
        manager = mock(Downloader.class);
        nextProcessor = mock(DownloadProcessor.class);
        downloader = new DownloadStarter(nextProcessor, manager, downloadFolder);
        task = new DownloadTask(SOURCE_URL);
    }

    @Test
    public void submitsTaskToDownloader() throws Exception {
        downloader.acceptTask(task);
        verify(manager).submit(task);
    }

    @Test
    public void fillsLocalPathForTask() throws Exception {
        File destPath = new File("/mnt/download/filename.mp3");
        when(downloadFolder.makePathForUrl(SOURCE_URL))
                .thenReturn(destPath);
        downloader.acceptTask(task);
        assertEquals(task.localPath, destPath);
    }

    @Test
    public void passesTaskToTrackerWithAnAssignedId() throws Exception {
        long taskId = 1;
        when(manager.submit(task)).thenReturn(taskId);
        downloader.acceptTask(task);
        assertEquals(taskId, task.id);
        verify(nextProcessor).acceptTask(task);
    }

    @Test
    public void ensuresDownloadFolderExists() throws Exception {
        downloader.acceptTask(task);
        verify(downloadFolder).mkdirs();
    }
}
