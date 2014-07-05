package org.dandelion.radiot.live.topics;

import org.dandelion.radiot.http.HttpClient;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HttpTopicProviderTest {
    public static final String SERVER_URL = "http://server.com";
    private final HttpClient httpClient = mock(HttpClient.class);
    private final HttpTopicProvider provider = new HttpTopicProvider(httpClient, SERVER_URL);

    @Test
    public void retrieveTopic() throws Exception {
        when(httpClient.getStringContent(SERVER_URL + "/chat/topic"))
                .thenReturn("{text: \"Lorem ipsum\"}");

        CurrentTopic topic = provider.get();
        assertThat(topic.text, equalTo("Lorem ipsum"));

    }
}