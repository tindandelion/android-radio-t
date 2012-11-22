package org.dandelion.radiot.endtoend.live;

import android.test.ActivityInstrumentationTestCase2;
import org.dandelion.radiot.helpers.HttpServer;
import org.dandelion.radiot.live.ui.LiveShowActivity;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;

public class ChatTranslationTest extends ActivityInstrumentationTestCase2<LiveShowActivity>{
    private LiveChatTranslationServer backend;

    public ChatTranslationTest() {
        super(LiveShowActivity.class);
    }

    public void testAtStartup_ConnectsToChatTranslation() throws Exception {
        openScreen();
        backend.hasReceivedRequest(equalTo("/data/jsonp?mode=last&recs=10"));
    }

    private void openScreen() {
        getActivity();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        backend = new LiveChatTranslationServer();
    }

    @Override
    public void tearDown() throws Exception {
        backend.stop();
        super.tearDown();
    }

    private static class LiveChatTranslationServer extends HttpServer {
        public LiveChatTranslationServer() throws IOException {
            super();
        }

        @Override
        protected Response serveUri(String uri) {
            return null;
        }
    }
}
