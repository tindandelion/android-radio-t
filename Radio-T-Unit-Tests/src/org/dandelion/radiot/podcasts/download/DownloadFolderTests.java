package org.dandelion.radiot.podcasts.download;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class DownloadFolderTests {
    public static final File LOCAL_DIR = new File("/mnt/downloads");
    private DownloadFolder folder;

    @Before
    public void setUp() throws Exception {
        folder = new DownloadFolder(LOCAL_DIR);
    }

    @Test
    public void constructsDestPathForUrlUsingItsBasename() throws Exception {
        String url = "http://example.com/my_file.mp3";
        File localPath = folder.makePathForUrl(url);
        assertEquals(new File(LOCAL_DIR, "my_file.mp3"), localPath);
    }
}
