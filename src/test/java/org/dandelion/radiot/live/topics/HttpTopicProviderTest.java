package org.dandelion.radiot.live.topics;

import org.dandelion.radiot.http.HttpClient;
import org.dandelion.radiot.http.NoContentException;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HttpTopicProviderTest {
    private static final String SERVER_URL = "http://server.com";
    private static final String REQUEST_URL = HttpTopicProvider.topicRequestUrl(SERVER_URL);

    private final HttpClient httpClient = mock(HttpClient.class);
    private final HttpTopicProvider provider = new HttpTopicProvider(httpClient, SERVER_URL);

    @Test
    public void whenHasData_returnsTopic() throws Exception {
        when(httpClient.getStringContent(REQUEST_URL))
                .thenReturn("{id: \"topic-id\", text: \"Lorem ipsum\"}");

        CurrentTopic topic = provider.get();
        assertThat(topic.id, equalTo("topic-id"));
        assertThat(topic.text, equalTo("Lorem ipsum"));

    }

    @Test
    public void whenNoData_returnsEmptyTopic() throws Exception {
        final String requestUrl = HttpTopicProvider.topicRequestUrl(SERVER_URL);
        when(httpClient.getStringContent(requestUrl)).thenThrow(noContent());

        CurrentTopic topic = provider.get();
        assertTrue(topic.isEmpty());
    }

    private NoContentException noContent() {
        return new NoContentException();
    }
}