package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ThumbnailLoaderTest {
    final static String URL = "http://radio-t.com/thumbnail.jpg";
    final static byte[] THUMBNAIL = new byte[0];

    final ThumbnailProvider thumbnailProvider = mock(ThumbnailProvider.class);
    final ThumbnailConsumer thumbnailConsumer = mock(ThumbnailConsumer.class);

    @Test
    public void feedsThumbnailToConsumer() throws Exception {
        final PodcastItem item = anItemWithThumbnailUrl();
        final PodcastList list = aListWith(item);

        when(thumbnailProvider.thumbnailDataFor(URL))
                .thenReturn(THUMBNAIL);

        final ThumbnailLoader loader = new ThumbnailLoader(list, thumbnailProvider, thumbnailConsumer);

        loader.retrieve();
        verify(thumbnailConsumer).consume(item, THUMBNAIL);
    }

    private PodcastItem anItemWithThumbnailUrl() {
        final PodcastItem item = new PodcastItem();
        item.setThumbnailUrl(URL);
        return item;
    }

    private static PodcastList aListWith(PodcastItem item) {
        final PodcastList list = new PodcastList();
        list.add(item);
        return list;
    }
}
