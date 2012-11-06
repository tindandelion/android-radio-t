package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.podcasts.core.PodcastList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import static org.mockito.Mockito.*;

public class PodcastListRetrieverTest {
    private final PodcastList cachedList = mock(PodcastList.class, "Cached list");
    private final PodcastList remoteList = mock(PodcastList.class, "Remote list");

    private final PodcastsProvider provider = mock(PodcastsProvider.class);
    private final PodcastsCache cache = mock(PodcastsCache.class);
    private final PodcastsConsumer consumer = mock(PodcastsConsumer.class);
    private final PodcastListRetriever retriever = new PodcastListRetriever(provider, cache, consumer);

    @Test
    public void whenCacheHasFreshData_RetrieveDataFromCacheAndNotBotherServer() throws Exception {
        cacheHasFreshData();
        retriever.retrieve();
        verify(consumer).updateList(cachedList);
        verifyZeroInteractions(provider);
    }

    @Test
    public void whenCacheHasInvalidData_ShouldPopulateDataFromCacheFirstAndThenFromServer() throws Exception {
        cacheHasInvalidData();

        retriever.retrieve();

        InOrder order = inOrder(consumer);
        order.verify(consumer).updateList(cachedList);
        order.verify(consumer).updateList(remoteList);
    }

    @Test
    public void whenTheCacheHasInvalidData_ShouldUpdateItWithNewData() throws Exception {
        cacheHasInvalidData();
        retriever.retrieve();
        verify(cache).updateWith(remoteList);
    }

    @Before
    public void setUp() throws Exception {
        when(provider.retrieve()).thenReturn(remoteList);
    }

    private void cacheHasInvalidData() {
        when(cache.hasValidData()).thenReturn(false);
        when(cache.getData()).thenReturn(cachedList);
    }

    private void cacheHasFreshData() {
        when(cache.hasValidData()).thenReturn(true);
        when(cache.getData()).thenReturn(cachedList);
    }
}
