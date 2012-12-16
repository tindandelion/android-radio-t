package org.dandelion.radiot.endtoend.live;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import com.jayway.android.robotium.solo.Solo;
import org.dandelion.radiot.helpers.HttpServer;
import org.dandelion.radiot.live.ui.ChatTranslationFragment;
import org.dandelion.radiot.live.chat.HttpChatTranslation;
import org.dandelion.radiot.live.ui.LiveShowActivity;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ChatTranslationTest extends ActivityInstrumentationTestCase2<LiveShowActivity>{
    private LiveChatTranslationServer backend;

    public ChatTranslationTest() {
        super(LiveShowActivity.class);
    }

    public void testAtStartup_RequestsChatContent() throws Exception {
        ChatTranslationRunner app = openScreen();
        backend.hasReceivedRequest(equalTo("/data/jsonp"));
        backend.respondSuccessWith(chatJsonWithMessages("Lorem ipsum", "Dolor sit amet"));

        app.showsChatMessage("Lorem ipsum");
        app.showsChatMessage("Dolor sit amet");
    }

    private String chatJsonWithMessages(String msg1, String msg2) {
        return String.format(
                "callback_fn({\"records\": [{\"msg\":\"%s\"},{\"msg\":\"%s\"}]});",
                msg1, msg2);
    }

    private ChatTranslationRunner openScreen() {
        return new ChatTranslationRunner(getInstrumentation(), getActivity());
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        backend = new LiveChatTranslationServer();
        ChatTranslationFragment.chat = new HttpChatTranslation(
                LiveChatTranslationServer.baseUrl());
    }

    @Override
    public void tearDown() throws Exception {
        backend.stop();
        super.tearDown();
    }

    //TODO: Duplication with RssTestServer
    private static class LiveChatTranslationServer extends HttpServer {
        private static final String MIME_JSON = "application/json";
        private BlockingQueue<Response> responseHolder = new LinkedBlockingDeque<Response>();

        public LiveChatTranslationServer() throws IOException {
            super();
        }

        @Override
        protected Response serveUri(String uri) {
            try {
                return responseHolder.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        public void respondSuccessWith(String content) {
            responseHolder.add(new Response(HTTP_OK, MIME_JSON, content));
        }
    }

    private static class ChatTranslationRunner extends Solo {
        public ChatTranslationRunner(Instrumentation instrumentation, Activity activity) {
            super(instrumentation, activity);
        }

        public void showsChatMessage(String message) {
            assertThat(this, showsText(message));
        }

        // TODO: Extract RobotiumMatchers?
        private Matcher<? super Solo> showsText(final String message) {
            return new TypeSafeMatcher<Solo>() {
                public static final long TIMEOUT_MS = 5000;

                @Override
                protected boolean matchesSafely(Solo solo) {
                    return solo.waitForText(message, 1, TIMEOUT_MS);
                }

                @Override
                public void describeTo(Description description) {
                    description.appendText("a text ").appendValue(message);
                }

                @Override
                protected void describeMismatchSafely(Solo item, Description mismatchDescription) {
                    mismatchDescription
                            .appendText("text ")
                            .appendValue(message)
                            .appendText(String.format(" didn't show up within %d milliseconds", TIMEOUT_MS));
                }
            };
        }
    }
}
