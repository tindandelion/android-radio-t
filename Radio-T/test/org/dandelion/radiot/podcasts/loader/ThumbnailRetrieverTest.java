package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;
import org.junit.Test;

import static org.dandelion.radiot.util.PodcastDataBuilder.*;
import static org.mockito.Mockito.*;

public class ThumbnailRetrieverTest {
    final static String URL = "http://radio-t.com/thumbnail.jpg";
    final static byte[] THUMBNAIL = new byte[0];

    private final ThumbnailProvider provider = mock(ThumbnailProvider.class);
    private final PodcastsConsumer consumer = mock(PodcastsConsumer.class);
    private final PodcastList list = anEmptyList();
    private ThumbnailRetriever retriever = new ThumbnailRetriever(list, provider, consumer);

    @Test
    public void feedsThumbnailToConsumer() throws Exception {
        final PodcastItem item = aPodcastItem(withThumbnailUrl(URL));
        list.add(item);

        when(provider.thumbnailDataFor(URL)).thenReturn(THUMBNAIL);
        retriever.retrieve(noInterrupts());

        verify(consumer).updateThumbnail(item, THUMBNAIL);
    }

    @Test
    public void whenNoThumbnailUrl_skipsItem() throws Exception {
        list.add(aPodcastItem(withThumbnailUrl(null)));

        retriever.retrieve(noInterrupts());
        verifyZeroInteractions(consumer);
    }

    private ThumbnailRetriever.Controller noInterrupts() {
        return new ThumbnailRetriever.Controller() {
            @Override
            public boolean isInterrupted() {
                return false;
            }
        };
    }
}
