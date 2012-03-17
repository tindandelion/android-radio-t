package org.dandelion.radiot.podcasts.download;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class DownloadStarterTests {
    public static final String SOURCE_FILENAME = "rt_podcast_1.mp3";
    public static final String SOURCE_URL = "http://radio-t.com/" + SOURCE_FILENAME;

    private Downloader manager;
    private DownloadStarter downloader;
    private DownloadFolder downloadFolder;
    private DownloadTracker tracker;

    @Before
    public void setUp() throws Exception {
        downloadFolder = mock(DownloadFolder.class);
        manager = mock(Downloader.class);
        tracker = mock(DownloadTracker.class);
        downloader = new DownloadStarter(manager, downloadFolder, tracker);
    }

    @Test
    public void providesPodcastUri() throws Exception {
        downloader.downloadPodcast(SOURCE_URL, "");
        verify(manager).submitRequest(eq(SOURCE_URL), anyFile());
    }

    @Test
    public void placesTaskIntoTracker() throws Exception {
        long taskId = 1;
        when(manager.submitRequest(anyString(), anyFile()))
                .thenReturn(taskId);
        downloader.downloadPodcast(SOURCE_URL, "");
        verify(tracker).taskScheduled(taskId);
    }

    @Test
    public void downloadsPodcastIntoDownloadFolder() throws Exception {
        File destPath = new File("/mnt/download/filename.mp3");
        when(downloadFolder.makePathForUrl(SOURCE_URL))
                .thenReturn(destPath);
        downloader.downloadPodcast(SOURCE_URL, "");
        verify(manager).submitRequest(anyString(), eq(destPath));
    }

    @Test
    public void ensureDestinationFolderExists() throws Exception {
        downloader.downloadPodcast(SOURCE_URL, "");
        verify(downloadFolder).ensureExists();
    }

    private static File anyFile() {
        return any(File.class);
    }
}
