package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.live.chat.RadiotRobolectricRunner;
import org.dandelion.radiot.podcasts.core.PodcastList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@RunWith(RadiotRobolectricRunner.class)
@SuppressWarnings("unchecked")
public class PodcastListClientTest {
    private final PodcastList serverList = mock(PodcastList.class, "server list");
    private final PodcastList cachedList = mock(PodcastList.class, "cached list");
    private final FakePodcastsCache podcastsCache = new FakePodcastsCache();
    private final PodcastsProvider podcasts = mock(PodcastsProvider.class);
    private final PodcastListClient client = new PodcastListClient(podcasts, podcastsCache, null, null);
    private PodcastsConsumer consumer = mock(PodcastsConsumer.class);

    @Test
    public void whenCacheIsInvalid_requestsDataFromServer() throws Exception {
        when(podcasts.retrieve()).thenReturn(serverList);

        client.populateData();

        verify(consumer).updateList(serverList);
    }

    @Test
    public void whenCacheIsValid_retrieveDataFromCache() throws Exception {
        podcastsCache.cachedData = cachedList;

        client.populateData();

        verify(consumer).updateList(cachedList);
        verify(podcasts, never()).retrieve();
    }

    @Test
    public void whenRetrievingNewData_ignoresCache() throws Exception {
        podcastsCache.cachedData = cachedList;
        PodcastList newList = new PodcastList();
        when(podcasts.retrieve()).thenReturn(newList);

        client.refreshData();

        verify(consumer).updateList(newList);
    }

    @Test
    public void whenRetrievingNewData_updatesCache() throws Exception {
        when(podcasts.retrieve()).thenReturn(serverList);

        client.refreshData();

        assertThat(podcastsCache.cachedData, equalTo(serverList));
    }

    @Test
    public void whenDataRetrievalFails_keepsDataInCache() throws Exception {
        podcastsCache.cachedData = cachedList;
        when(podcasts.retrieve()).thenThrow(Exception.class);

        client.refreshData();

        assertThat(podcastsCache.cachedData, equalTo(serverList));
    }

    @Before
    public void setUp() throws Exception {
        client.attach(ProgressListener.Null, consumer);
    }

    private static class FakePodcastsCache implements PodcastsCache {
        public PodcastList cachedData = null;
        @Override
        public boolean hasValidData() {
            return cachedData != null;
        }

        @Override
        public PodcastList getData() {
            return cachedData;
        }

        @Override
        public void updateWith(PodcastList data) {
            cachedData = data;
        }

        @Override
        public void reset() {
            cachedData = null;
        }
    }
}
