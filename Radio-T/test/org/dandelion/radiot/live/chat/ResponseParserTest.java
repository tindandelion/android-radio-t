package org.dandelion.radiot.live.chat;

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
        List<Message> messages = ResponseParser.parse(strJson);
        assertTrue(messages.isEmpty());
    }

    @Test
    public void extractMessageList() throws Exception {
        String strJson = chatStream(withMessages(
                chatMessage("sender1", "Lorem ipsum", "Sat Dec 15 22:19:27 UTC 2012"),
                chatMessage("sender2", "Dolor sit amet", "Sat Dec 15 00:15:27 UTC 2012"),
                chatMessage("sender3","Consectur", "")));
        List<Message> messages = ResponseParser.parse(strJson);

        assertThat(messages, hasItem(new Message("sender1", "Lorem ipsum", "01:19")));
        assertThat(messages, hasItem(new Message("sender2", "Dolor sit amet", "03:15")));
        assertThat(messages, hasItem(new Message("sender3", "Consectur", "")));
    }
}
