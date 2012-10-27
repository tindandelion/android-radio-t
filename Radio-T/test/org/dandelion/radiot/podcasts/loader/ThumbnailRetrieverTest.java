package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ThumbnailRetrieverTest {
    final static String URL = "http://radio-t.com/thumbnail.jpg";
    final static byte[] THUMBNAIL = new byte[0];

    final ThumbnailProvider provider = mock(ThumbnailProvider.class);
    final PodcastsConsumer consumer = mock(PodcastsConsumer.class);

    @Test
    public void feedsThumbnailToConsumer() throws Exception {
        final PodcastItem item = anItemWithThumbnailUrl(URL);
        final PodcastList list = aListWith(item);

        when(provider.thumbnailDataFor(URL))
                .thenReturn(THUMBNAIL);

        final ThumbnailRetriever retriever = new ThumbnailRetriever(list, provider, consumer);

        retriever.retrieveNext();
        verify(consumer).updateThumbnail(item, THUMBNAIL);
    }

    @Test
    public void whenNoThumbnailUrl_skipsItem() throws Exception {
        final PodcastList list = aListWith(anItemWithThumbnailUrl(null));

        final ThumbnailRetriever retriever = new ThumbnailRetriever(list, provider, consumer);

        retriever.retrieveNext();
        verifyZeroInteractions(consumer);
    }

    private PodcastItem anItemWithThumbnailUrl(String url) {
        final PodcastItem item = new PodcastItem();
        item.setThumbnailUrl(url);
        return item;
    }

    private static PodcastList aListWith(PodcastItem item) {
        final PodcastList list = new PodcastList();
        list.add(item);
        return list;
    }
}
