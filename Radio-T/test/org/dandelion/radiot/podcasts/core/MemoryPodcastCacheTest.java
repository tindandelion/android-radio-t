package org.dandelion.radiot.podcasts.core;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class MemoryPodcastCacheTest {
    private final PodcastsProvider provider = mock(PodcastsProvider.class);
    private final MemoryPodcastCache cache = new MemoryPodcastCache(provider);
    private final List<PodcastItem> list = new ArrayList<PodcastItem>();

    @Test
    public void retrievesListFromProviderForTheFirstCall() throws Exception {
        when(provider.retrieveAll()).thenReturn(list);
        assertThat(cache.retrieveAll(), equalTo(list));
    }

    @Test
    public void callsProviderOnceAndCachesTheList() throws Exception {
        when(provider.retrieveAll()).thenReturn(list);

        assertThat(cache.retrieveAll(), equalTo(list));
        assertThat(cache.retrieveAll(), equalTo(list));
        verify(provider, times(1)).retrieveAll();
    }
}
