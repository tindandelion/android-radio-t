package org.dandelion.radiot.podcasts.download;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;
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
    private DownloadStarter.Listener listener;

    @Before
    public void setUp() throws Exception {
        downloadFolder = mock(DownloadFolder.class);
        manager = mock(Downloader.class);
        listener = mock(DownloadStarter.Listener.class);
        downloader = new DownloadStarter(manager, downloadFolder);
        downloader.setListener(listener);
    }

    @Test
    public void providesPodcastUri() throws Exception {
        downloader.downloadPodcast(SOURCE_URL);
        verify(manager).submitRequest(eq(SOURCE_URL), anyFile());
    }

    private File anyFile() {
        return any(File.class);
    }

    @Test
    public void downloadsPodcastIntoDownloadFolder() throws Exception {
        File destPath = new File("/mnt/download/filename.mp3");
        when(downloadFolder.makePathForUrl(SOURCE_URL))
                .thenReturn(destPath);
        downloader.downloadPodcast(SOURCE_URL);
        verify(manager).submitRequest(anyString(), eq(destPath));
    }

    @Test
    public void signalsListenerWhenAllDownloadsCompleted() throws Exception {
        long id1 = 1;
        long id2 = 2;
        when(manager.submitRequest(anyString(), anyFile()))
                .thenReturn(id1, id2);

        downloader.downloadPodcast(SOURCE_URL);
        downloader.downloadPodcast(SOURCE_URL);

        downloader.downloadCompleted(id1);
        verifyZeroInteractions(listener);

        downloader.downloadCompleted(id2);
        verify(listener).onFinishedAllDownloads();
    }

    @Test
    public void skipsForeignDownloadTasks() throws Exception {
        long myId = 1;
        long foreignId = 2;
        when(manager.submitRequest(anyString(), anyFile()))
                .thenReturn(myId);

        downloader.downloadPodcast(SOURCE_URL);
        downloader.downloadCompleted(foreignId);
        
        assertTrue(downloader.hasDownloadsInProgress());

    }

    @Test
    public void ensureDestinationFolderExists() throws Exception {
        downloader.downloadPodcast(SOURCE_URL);
        verify(downloadFolder).ensureExists();
    }
}
