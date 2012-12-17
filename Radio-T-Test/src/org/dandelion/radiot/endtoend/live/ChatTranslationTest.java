package org.dandelion.radiot.endtoend.live;

import android.test.ActivityInstrumentationTestCase2;
import org.dandelion.radiot.endtoend.live.helpers.ChatTranslationRunner;
import org.dandelion.radiot.endtoend.live.helpers.LiveChatTranslationServer;
import org.dandelion.radiot.live.chat.HttpChatTranslation;
import org.dandelion.radiot.live.ui.ChatTranslationFragment;
import org.dandelion.radiot.live.ui.LiveShowActivity;

import static org.hamcrest.CoreMatchers.equalTo;

public class ChatTranslationTest extends ActivityInstrumentationTestCase2<LiveShowActivity> {
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

}

