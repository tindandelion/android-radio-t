package org.dandelion.radiot.podcasts.download;

import org.junit.Test;

import java.io.File;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DownloadProcessorTest {
    public static final String TITLE = "Podcast";
    private static final File PATH = new File("/mnt/download/filename.mp3");

    private final MediaScanner scanner = mock(MediaScanner.class);
    private final NotificationManager notificationManager = mock(NotificationManager.class);
    private final DownloadProcessor processor = new DownloadProcessor(scanner, notificationManager);

    @Test
    public void showError() {
        final int errorCode = 1006;
        processor.downloadError(TITLE, errorCode);
        verify(notificationManager).showError(TITLE, errorCode);
    }

    @Test
    public void scanFileWithMediaScannerWhenComplete() throws Exception {
        processor.downloadComplete(TITLE, PATH);
        verify(scanner).scanAudioFile(PATH);
    }

    @Test
    public void showSuccessWhenDownloadComplete() throws Exception {
        processor.downloadComplete(TITLE, PATH);
        verify(notificationManager).showSuccess(TITLE, PATH);
    }
}
