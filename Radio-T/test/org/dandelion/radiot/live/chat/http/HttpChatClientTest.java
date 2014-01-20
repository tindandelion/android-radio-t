package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.http.HttpClient;
import org.dandelion.radiot.live.chat.Message;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.io.IOException;
import java.util.List;

import static org.dandelion.radiot.util.ChatStreamBuilder.chatStream;
import static org.dandelion.radiot.util.ChatStreamBuilder.message;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HttpChatClientTest {
    private static final String CHAT_URL = "http://chat.test.com";

    private final HttpClient httpClient = mock(HttpClient.class);
    private final HttpChatClient client = new HttpChatClient(CHAT_URL, httpClient);

    @Test
    public void whenNoMessages_returnsEmptyMessageList() throws Exception {
        whenRequestingLastMessages(httpClient).thenReturn(chatStream());

        List<Message> messages = client.retrieveLastMessages();
        assertThat(messages, is(empty()));
        assertThat(client.lastMessageSeq(), is(equalTo(0)));
    }

    @Test
    public void whenHasMessages_parseMessagesIntoList() throws Exception {
        whenRequestingLastMessages(httpClient)
                .thenReturn(chatStream(
                        message("sender1", "Lorem ipsum", "Sat Dec 15 22:19:27 UTC 2012", 10),
                        message("sender2", "Dolor sit amet", "Sat Dec 15 00:15:27 UTC 2012", 11),
                        message("sender3", "Consectur", "", 12)));


        List<Message> messages = client.retrieveLastMessages();
        assertThat(messages, hasItem(new Message("sender1", "Lorem ipsum", "01:19", 10)));
        assertThat(messages, hasItem(new Message("sender2", "Dolor sit amet", "03:15", 11)));
        assertThat(messages, hasItem(new Message("sender3", "Consectur", "", 12)));
    }

    @Test
    public void whenRequestingLastMessages_recordsLastMessageSeq() throws Exception {
        whenRequestingLastMessages(httpClient)
                .thenReturn(chatStream(
                        message(10, "message1"),
                        message(11, "message2")));

        client.retrieveLastMessages();
        assertThat(client.lastMessageSeq(), is(equalTo(11)));
    }

    @Test
    public void whenRequestingNewMessages_usesLastMessageSeq() throws Exception {
        whenRequestingLastMessages(httpClient)
                .thenReturn(chatStream(message(10, "lorem ipsum")));

        whenRequestingNewMessages(httpClient).thenReturn(chatStream());

        client.retrieveLastMessages();
        client.retrieveNewMessages(0);

        verify(httpClient);
    }

    @Test
    public void whenRequestingNewMessages_updatesLastMessageSeq() throws Exception {
        whenRequestingLastMessages(httpClient)
                .thenReturn(chatStream(message(10, "lorem ipsum")));

        whenRequestingNewMessages(httpClient)
                .thenReturn(chatStream(message(11, "dolor sit amet")));

        client.retrieveLastMessages();
        client.retrieveNewMessages(0);

        assertThat(client.lastMessageSeq(), is(Matchers.equalTo(11)));
    }

    private OngoingStubbing<String> whenRequestingNewMessages(HttpClient httpClient1) throws IOException {
        String url = HttpChatClient.newRecordsUrl(CHAT_URL, 10);
        return when(httpClient1.getStringContent(url));
    }

    private OngoingStubbing<String> whenRequestingLastMessages(HttpClient httpClient) throws IOException {
        String url = HttpChatClient.lastRecordsUrl(CHAT_URL);
        return when(httpClient.getStringContent(url));
    }
}
