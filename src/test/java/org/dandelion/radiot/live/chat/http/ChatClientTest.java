package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.http.HttpClient;
import org.dandelion.radiot.live.chat.HttpChatClient;
import org.dandelion.radiot.live.chat.Message;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.io.IOException;
import java.util.List;

import static org.dandelion.radiot.util.ChatStreamBuilder.chatStream;
import static org.dandelion.radiot.util.ChatStreamBuilder.message;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class ChatClientTest {
    private static final String CHAT_URL = "http://chat.test.com";

    private final HttpClient httpClient = mock(HttpClient.class);
    private final HttpChatClient client = new HttpChatClient(CHAT_URL, httpClient);

    @Test
    public void whenRetievingFirstTime_askForLastMessages() throws Exception {
        whenRequestingLastMessages(httpClient).thenReturn(chatStream());

        client.get();

        verify(httpClient).getStringContent(HttpChatClient.lastRecordsUrl(CHAT_URL));
    }

    @Test
    public void whenNoMessages_returnsEmptyMessageList() throws Exception {
        whenRequestingLastMessages(httpClient).thenReturn(chatStream());

        List<Message> messages = client.get();
        assertThat(messages, is(empty()));
    }

    @Test
    public void whenHasMessages_parseMessagesIntoList() throws Exception {
        whenRequestingLastMessages(httpClient)
                .thenReturn(chatStream(
                        message("sender1", "Lorem ipsum", "Sat Dec 15 22:19:27 UTC 2012", 10),
                        message("sender2", "Dolor sit amet", "Sat Dec 15 00:15:27 UTC 2012", 11),
                        message("sender3", "Consectur", "", 12)));


        List<Message> messages = client.get();
        assertThat(messages, hasItem(new Message("sender1", "Lorem ipsum", "01:19", 10)));
        assertThat(messages, hasItem(new Message("sender2", "Dolor sit amet", "03:15", 11)));
        assertThat(messages, hasItem(new Message("sender3", "Consectur", "", 12)));
    }

    @Test
    public void whenRetrievingSubsequentMessages_askForNewMessages() throws Exception {
        final int LAST_SEQ = 11;
        whenRequestingLastMessages(httpClient).thenReturn(
                chatStream(
                        message(LAST_SEQ-1, "lorem ipsum"),
                        message(LAST_SEQ, "dolor sit amet")));
        whenRequestingNewMessages(httpClient, LAST_SEQ).thenReturn(chatStream());

        client.get();
        client.get();

        verify(httpClient).getStringContent(HttpChatClient.lastRecordsUrl(CHAT_URL));
        verify(httpClient).getStringContent(HttpChatClient.newRecordsUrl(CHAT_URL, LAST_SEQ));
    }

    @Test
    public void eachSubsequentMessageRetrieval_usesLastMessageSeqFromPreviousCall() throws Exception {
        whenRequestingLastMessages(httpClient).thenReturn(
                chatStream(
                        message(10, "lorem ipsum"),
                        message(11, "dolor sit amet")));

        whenRequestingNewMessages(httpClient, 11).thenReturn(
                chatStream(message(12, "")));

        whenRequestingNewMessages(httpClient, 12).thenReturn(
                chatStream(message(13, "")));

        client.get();
        client.get();
        client.get();

        verify(httpClient).getStringContent(HttpChatClient.lastRecordsUrl(CHAT_URL));
        verify(httpClient).getStringContent(HttpChatClient.newRecordsUrl(CHAT_URL, 11));
        verify(httpClient).getStringContent(HttpChatClient.newRecordsUrl(CHAT_URL, 12));
    }

    private OngoingStubbing<String> whenRequestingNewMessages(HttpClient httpClient, int lastMessageSeq) throws IOException {
        String url = HttpChatClient.newRecordsUrl(CHAT_URL, lastMessageSeq);
        return when(httpClient.getStringContent(url));
    }

    private OngoingStubbing<String> whenRequestingLastMessages(HttpClient httpClient) throws IOException {
        String url = HttpChatClient.lastRecordsUrl(CHAT_URL);
        return when(httpClient.getStringContent(url));
    }
}
