package org.dandelion.radiot.live.chat;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.json.JSONArray;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;

import java.util.List;

import static org.dandelion.radiot.util.ChatStreamBuilder.chatMessage;
import static org.dandelion.radiot.util.ChatStreamBuilder.chatStream;
import static org.dandelion.radiot.util.ChatStreamBuilder.withMessages;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.hasItem;

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

    private Matcher<Iterable<ChatMessage>> hasMessage(ChatMessage message) {
        return hasItem(new ChatMessageMatcher(message));
    }

    private class ChatMessageMatcher extends TypeSafeMatcher<ChatMessage> {
        private final ChatMessage expected;

        public ChatMessageMatcher(ChatMessage expected) {
            this.expected = expected;
        }

        @Override
        public boolean matchesSafely(ChatMessage actual) {
            return actual.body.equals(expected.body) &&
                    actual.sender.equals(expected.sender);
        }

        @Override
        public void describeTo(Description description) {
            description
                    .appendText("a chat message with sender: ")
                    .appendValue(expected.sender)
                    .appendText(" and body: ")
                    .appendValue(expected.body);
        }
    }
}
