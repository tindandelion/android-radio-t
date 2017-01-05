package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.http.HttpClient;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;
import org.junit.Test;
import org.mockito.InOrder;

import java.io.IOException;

import static org.dandelion.radiot.util.PodcastDataBuilder.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class ThumbnailRetrieverTest {
    final static String REMOTE_URL = "http://radio-t.com/thumbnail1.jpg";
    final static String CACHED_URL = "http://radio-t.com/thumbnail2.jpg";

    final static byte[] THUMBNAIL = new byte[0];

    private final HttpClient httpClient = mock(HttpClient.class);
    private final PodcastsConsumer consumer = mock(PodcastsConsumer.class);
    private final ThumbnailCache cache = mock(ThumbnailCache.class);
    private final PodcastList list = anEmptyList();
    private ThumbnailRetriever retriever = new ThumbnailRetriever(httpClient, cache);

    @Test
    public void whenThumbnailIsCached_willFetchItFromCache() throws Exception {
        final PodcastItem item = aPodcastItem(withThumbnailUrl(CACHED_URL));
        list.add(item);

        thumbnailIsCached(CACHED_URL, THUMBNAIL);
        retriever.retrieve(list, consumer, noInterrupts());

        verify(cache).lookup(CACHED_URL);
        verify(httpClient, never()).getByteContent(CACHED_URL);
        verify(consumer).updateThumbnail(item, THUMBNAIL);
    }

    @Test
    public void whenThumbnailIsNotCached_willDownloadIt() throws Exception {
        final PodcastItem item = aPodcastItem(withThumbnailUrl(REMOTE_URL));
        list.add(item);

        thumbnailIsRemote(REMOTE_URL, THUMBNAIL);
        retriever.retrieve(list, consumer, noInterrupts());

        verify(httpClient).getByteContent(REMOTE_URL);
        verify(consumer).updateThumbnail(item, THUMBNAIL);
    }

    @Test
    public void whenDownloadsThumbnail_willStoreItInCache() throws Exception {
        final PodcastItem item = aPodcastItem(withThumbnailUrl(REMOTE_URL));
        list.add(item);

        thumbnailIsRemote(REMOTE_URL, THUMBNAIL);
        retriever.retrieve(list, consumer, noInterrupts());

        verify(httpClient).getByteContent(REMOTE_URL);
        verify(cache).update(REMOTE_URL, THUMBNAIL);
    }

    @Test
    public void whenRetrievingThumbnails_firstRetrieveAllCached_thenDownloadRemotes() throws Exception {
        final PodcastItem remote = aPodcastItem(withThumbnailUrl(REMOTE_URL));
        final PodcastItem cached = aPodcastItem(withThumbnailUrl(CACHED_URL));
        list.add(remote);
        list.add(cached);

        thumbnailIsRemote(REMOTE_URL, THUMBNAIL);
        thumbnailIsCached(CACHED_URL, THUMBNAIL);
        retriever.retrieve(list, consumer, noInterrupts());

        InOrder order = inOrder(consumer);
        order.verify(consumer).updateThumbnail(cached, THUMBNAIL);
        order.verify(consumer).updateThumbnail(remote, THUMBNAIL);
    }

    @Test
    public void whenNoThumbnailUrl_skipsItemEntirely() throws Exception {
        list.add(aPodcastItem(withThumbnailUrl(null)));

        retriever.retrieve(list, consumer, noInterrupts());
        verifyZeroInteractions(cache, httpClient, consumer);
    }

    @Test
    public void whenFailsToRetrieveThumbnail_doesNotProcessItem() throws Exception {
        final PodcastItem item = aPodcastItem(withThumbnailUrl(REMOTE_URL));
        list.add(item);

        when(cache.lookup(REMOTE_URL)).thenReturn(null);
        when(httpClient.getByteContent(REMOTE_URL)).thenThrow(Exception.class);

        retriever.retrieve(list, consumer, noInterrupts());

        verify(cache, never()).update(eq(REMOTE_URL), any(byte[].class));
        verify(consumer, never()).updateThumbnail(eq(item), any(byte[].class));
    }

    private void thumbnailIsCached(String url, byte[] data) {
        when(cache.lookup(url)).thenReturn(data);
    }

    private void thumbnailIsRemote(String url, byte[] data) throws IOException {
        when(cache.lookup(url)).thenReturn(null);
        when(httpClient.getByteContent(url)).thenReturn(data);
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
