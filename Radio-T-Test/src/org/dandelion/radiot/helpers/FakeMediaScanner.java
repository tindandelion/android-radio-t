package org.dandelion.radiot.helpers;

import junit.framework.Assert;
import org.dandelion.radiot.podcasts.download.MediaScanner;

import java.io.File;

public class FakeMediaScanner implements MediaScanner {
    SyncValueHolder<File> scannedFile = new SyncValueHolder<File>();

    @Override
    public void scanAudioFile(File path) {
        scannedFile.setValue(path);
    }

    public void assertScannedFile(File path) {
        Assert.assertEquals("Scanned file", path, scannedFile.getValue());
    }

    public void assertNoInteractions() {
        Assert.assertFalse("Unexpected file scan", scannedFile.await());
    }
}
