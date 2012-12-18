package org.dandelion.radiot.live.chat;

import org.hamcrest.Matcher;
import org.json.JSONArray;
import org.junit.Test;

import java.util.List;

import static org.dandelion.radiot.util.ChatStreamBuilder.*;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ResponseParserTest {
    @Test
    public void parseEmptyRecordList() throws Exception {
        String strJson = chatStream(new JSONArray());
        List<ChatMessage> messages = ResponseParser.parse(strJson);
        assertTrue(messages.isEmpty());
    }

    @Test
    public void extractMessageList() throws Exception {
        String strJson = chatStream(withMessages(
                chatMessage("sender1", "Lorem ipsum"),
                chatMessage("sender2", "Dolor sit amet"),
                chatMessage("sender3","Consectur")));
        List<ChatMessage> messages = ResponseParser.parse(strJson);

        assertThat(messages, hasMessage(new ChatMessage("sender1", "Lorem ipsum")));
        assertThat(messages, hasMessage(new ChatMessage("sender2", "Dolor sit amet")));
        assertThat(messages, hasMessage(new ChatMessage("sender3", "Consectur")));
    }

    private Matcher<? super List<ChatMessage>> hasMessage(ChatMessage expected) {
        return hasItem(expected);
    }
}
