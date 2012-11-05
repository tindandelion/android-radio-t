package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;
import org.junit.Test;
import org.mockito.InOrder;

import static org.dandelion.radiot.util.PodcastDataBuilder.*;
import static org.mockito.Mockito.*;

public class ThumbnailRetrieverTest {
    final static String REMOTE_URL = "http://radio-t.com/thumbnail1.jpg";
    final static String CACHED_URL = "http://radio-t.com/thumbnail2.jpg";

    final static byte[] THUMBNAIL = new byte[0];

    private final ThumbnailProvider provider = mock(ThumbnailProvider.class);
    private final PodcastsConsumer consumer = mock(PodcastsConsumer.class);
    private final ThumbnailCache cache = mock(ThumbnailCache.class);
    private final PodcastList list = anEmptyList();
    private ThumbnailRetriever retriever = new ThumbnailRetriever(provider, cache, consumer);

    @Test
    public void whenThumbnailIsCached_WillFetchItFromCache() throws Exception {
        final PodcastItem item = aPodcastItem(withThumbnailUrl(CACHED_URL));
        list.add(item);

        thumbnailIsCached(CACHED_URL, THUMBNAIL);
        retriever.retrieve(list, noInterrupts());

        verify(cache).lookup(CACHED_URL);
        verify(consumer).updateThumbnail(item, THUMBNAIL);
    }

    @Test
    public void whenThumbnailIsNotCached_willDownloadIt() throws Exception {
        final PodcastItem item = aPodcastItem(withThumbnailUrl(REMOTE_URL));
        list.add(item);

        thumbnailIsRemote(REMOTE_URL, THUMBNAIL);
        retriever.retrieve(list, noInterrupts());

        verify(provider).thumbnailDataFor(REMOTE_URL);
        verify(consumer).updateThumbnail(item, THUMBNAIL);
    }

    @Test
    public void whenDownloadsThumbnail_WillStoreItInCache() throws Exception {
        final PodcastItem item = aPodcastItem(withThumbnailUrl(REMOTE_URL));
        list.add(item);

        thumbnailIsRemote(REMOTE_URL, THUMBNAIL);
        retriever.retrieve(list, noInterrupts());

        verify(provider).thumbnailDataFor(REMOTE_URL);
        verify(cache).update(REMOTE_URL, THUMBNAIL);
    }

    @Test
    public void whenRetrievingThumbnails_FirstRetrieveAllCached_ThenDownloadRemotes() throws Exception {
        final PodcastItem remote = aPodcastItem(withThumbnailUrl(REMOTE_URL));
        final PodcastItem cached = aPodcastItem(withThumbnailUrl(CACHED_URL));
        list.add(remote);
        list.add(cached);

        thumbnailIsRemote(REMOTE_URL, THUMBNAIL);
        thumbnailIsCached(CACHED_URL, THUMBNAIL);
        retriever.retrieve(list, noInterrupts());

        InOrder order = inOrder(consumer);
        order.verify(consumer).updateThumbnail(cached, THUMBNAIL);
        order.verify(consumer).updateThumbnail(remote, THUMBNAIL);
    }

    @Test
    public void whenNoThumbnailUrl_SkipsItemEntirely() throws Exception {
        list.add(aPodcastItem(withThumbnailUrl(null)));

        retriever.retrieve(list, noInterrupts());
        verifyZeroInteractions(cache, provider, consumer);
    }

    private void thumbnailIsCached(String url, byte[] data) {
        when(cache.lookup(url)).thenReturn(data);
    }

    private void thumbnailIsRemote(String url, byte[] data) {
        when(cache.lookup(url)).thenReturn(null);
        when(provider.thumbnailDataFor(url)).thenReturn(data);
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
