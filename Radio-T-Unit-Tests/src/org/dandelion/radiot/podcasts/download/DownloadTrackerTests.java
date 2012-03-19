package org.dandelion.radiot.podcasts.download;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class DownloadTrackerTests {
    private DownloadTracker tracker;
    private DownloadTask task;
    private DownloadProcessor nextProcessor;
    private Downloader downloader;

    @Before
    public void setUp() throws Exception {
        nextProcessor = mock(DownloadProcessor.class);
        downloader = mock(Downloader.class);
        tracker = new DownloadTracker(nextProcessor, downloader);
        task = new DownloadTask().setId(1);
    }

    @Test
    public void queriesTaskFromDownloadManagerAndPassesItFurther() throws Exception {
        when(downloader.query(1)).thenReturn(task);
        tracker.onDownloadComplete(1);
        verify(nextProcessor).acceptTask(task);
    }

    @Test
    public void skipsCancelledTasks() throws Exception {
        when(downloader.query(1)).thenReturn(null);
        tracker.onDownloadComplete(1);
        verifyZeroInteractions(nextProcessor);
    }
}
