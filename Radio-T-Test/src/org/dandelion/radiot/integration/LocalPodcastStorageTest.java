package org.dandelion.radiot.integration;

import org.dandelion.radiot.integration.helpers.FileUtils;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.loader.PodcastsCache;
import org.dandelion.radiot.podcasts.loader.ThumbnailCache;
import org.dandelion.radiot.podcasts.loader.caching.LocalPodcastStorage;

import static org.dandelion.radiot.util.PodcastDataBuilder.*;

public class LocalPodcastStorageTest extends FilesystemTestCase {
    private LocalPodcastStorage storage;

    public void testWhenSavingPodcasts_RemovesRedundantThumbnails() throws Exception {
        final String url = "/thumbnail.url";
        final PodcastItem item = aPodcastItem(withThumbnailUrl(url));
        final byte[] thumbnail = new byte[0];

        PodcastsCache podcastsCache = storage.podcastsCache();
        ThumbnailCache thumbnailCache = storage.thumbnailsCache();

        podcastsCache.updateWith(aListWith(item));
        thumbnailCache.update(url, thumbnail);
        assertNotNull(thumbnailCache.lookup(url));

        podcastsCache.updateWith(anEmptyList());
        assertNull(thumbnailCache.lookup(url));
    }

    public void setUp() throws Exception {
        super.setUp();
        FileUtils.mkdir(workDir);
        storage = new LocalPodcastStorage("test-show", workDir);
    }
}
