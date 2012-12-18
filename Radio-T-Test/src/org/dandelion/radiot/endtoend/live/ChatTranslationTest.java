package org.dandelion.radiot.endtoend.live;

import android.test.ActivityInstrumentationTestCase2;
import org.dandelion.radiot.endtoend.live.helpers.ChatTranslationRunner;
import org.dandelion.radiot.endtoend.live.helpers.LiveChatTranslationServer;
import org.dandelion.radiot.live.chat.HttpChatTranslation;
import org.dandelion.radiot.live.ui.ChatTranslationFragment;
import org.dandelion.radiot.live.ui.LiveShowActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static org.dandelion.radiot.endtoend.live.ChatStreamBuilder.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class ChatTranslationTest extends ActivityInstrumentationTestCase2<LiveShowActivity> {
    private LiveChatTranslationServer backend;

    public ChatTranslationTest() {
        super(LiveShowActivity.class);
    }

    public void testAtStartup_RequestsChatContent() throws Exception {
        ChatTranslationRunner app = openScreen();
        backend.hasReceivedRequest(equalTo("/data/jsonp"));
        backend.respondWithChatStream(chatStream(withMessages("Lorem ipsum", "Dolor sit amet")));

        app.showsChatMessage("Lorem ipsum");
        app.showsChatMessage("Dolor sit amet");
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

class ChatStreamBuilder {
    public static JSONObject chatStream(JSONArray messages) throws JSONException {
        JSONObject result = new JSONObject();
        result.put("records", messages);
        return result;
    }

    public static JSONArray withMessages(String... msgs) throws JSONException {
        JSONArray array = new JSONArray();
        for (String msg : msgs) {
            array.put(messageWithBody(msg));
        }
        return array;
    }

    public static JSONObject messageWithBody(String body) throws JSONException {
        JSONObject message = new JSONObject();
        message.put("msg", body);
        return message;
    }
}

