package org.dandelion.radiot.live.chat;

import org.hamcrest.Description;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;

import java.util.List;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.hasItem;

public class ResponseParserTest {
    @Test
    public void parseEmptyRecordList() throws Exception {
        String strJson = "callback_fn({\"records\": []})";
        List<ChatMessage> messages = ResponseParser.parse(strJson);
        assertTrue(messages.isEmpty());
    }

    @Test
    public void extractMessageList() throws Exception {
        String strJson = "callback_fn({\"records\": [{\"msg\": \"Lorem ipsum\"}, {\"msg\": \"Dolor sit amet\"}, {\"msg\": \"Consectur\"}]})";
        List<ChatMessage> messages = ResponseParser.parse(strJson);

        assertThat(messages, hasItem(withBody("Lorem ipsum")));
        assertThat(messages, hasItem(withBody("Dolor sit amet")));
        assertThat(messages, hasItem(withBody("Consectur")));
    }

    private MessageBodyMatcher withBody(String body) {
        return new MessageBodyMatcher(body);
    }

    private class MessageBodyMatcher extends TypeSafeMatcher<ChatMessage> {
        private final String expectedBody;

        public MessageBodyMatcher(String expected) {
            this.expectedBody = expected;
        }

        @Override
        public boolean matchesSafely(ChatMessage actual) {
            return actual.body.equals(expectedBody);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("a chat message with body: ").appendValue(expectedBody);
        }
    }
}
