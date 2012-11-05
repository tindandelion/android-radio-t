package org.dandelion.radiot.integration;

import android.test.InstrumentationTestCase;
import org.dandelion.radiot.integration.helpers.FileUtils;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.loader.PodcastsCache;
import org.dandelion.radiot.podcasts.loader.ThumbnailCache;
import org.dandelion.radiot.podcasts.loader.caching.LocalPodcastStorage;

import java.io.File;

import static org.dandelion.radiot.util.PodcastDataBuilder.*;

public class LocalPodcastStorageTest extends InstrumentationTestCase {
    private File workDir;
    private LocalPodcastStorage storage;

    public void testWhenSavingPodcasts_RemovesRedundantThumbnails() throws Exception {
        final String url = "/thumbnail.url";
        PodcastsCache podcastsCache = storage.podcastsCache();
        ThumbnailCache thumbnailCache = storage.thumbnailsCache();

        final PodcastItem item = aPodcastItem(withThumbnailUrl(url));
        podcastsCache.updateWith(aListWith(item));
        thumbnailCache.update(url, new byte[0]);
        assertNotNull(thumbnailCache.lookup(url));

        podcastsCache.updateWith(anEmptyList());
        assertNull(thumbnailCache.lookup(url));
    }

    protected void setUp() throws Exception {
        super.setUp();
        workDir = new File(getInstrumentation().getTargetContext().getCacheDir(), "work-dir");
        FileUtils.deleteDir(workDir);
        FileUtils.mkdir(workDir);
        storage = new LocalPodcastStorage("test-show", workDir);
    }

    @Override
    public void tearDown() throws Exception {
        FileUtils.deleteDir(workDir);
        super.tearDown();
    }

}
