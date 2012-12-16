package org.dandelion.radiot.live.chat;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.hasItem;

public class ResponseParserTest {
    @Test
    public void parseEmptyRecordList() throws Exception {
        String strJson = "callback_fn({\"records\": []})";
        List<String> messages = ResponseParser.parse(strJson);
        assertTrue(messages.isEmpty());
    }

    @Test
    public void extractMessageList() throws Exception {
        String strJson = "callback_fn({\"records\": [{\"msg\": \"Lorem ipsum\"}, {\"msg\": \"Dolor sit amet\"}, {\"msg\": \"Consectur\"}]})";
        List<String> messages = ResponseParser.parse(strJson);

        assertThat(messages, hasItem("Lorem ipsum"));
        assertThat(messages, hasItem("Dolor sit amet"));
        assertThat(messages, hasItem("Consectur"));
    }
}
