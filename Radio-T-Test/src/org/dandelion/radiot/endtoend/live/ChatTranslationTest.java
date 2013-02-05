package org.dandelion.radiot.endtoend.live;

import android.test.ActivityInstrumentationTestCase2;
import org.dandelion.radiot.endtoend.live.helpers.ChatTranslationRunner;
import org.dandelion.radiot.endtoend.live.helpers.LiveChatTranslationServer;
import org.dandelion.radiot.live.chat.ChatTranslation;
import org.dandelion.radiot.live.chat.HttpChatTranslation;
import org.dandelion.radiot.live.schedule.DeterministicScheduler;
import org.dandelion.radiot.live.ui.ChatTranslationFragment;
import org.dandelion.radiot.live.ui.LiveShowActivity;

import static org.dandelion.radiot.util.ChatStreamBuilder.chatStream;
import static org.dandelion.radiot.util.ChatStreamBuilder.withMessages;

public class ChatTranslationTest extends ActivityInstrumentationTestCase2<LiveShowActivity> {
    private LiveChatTranslationServer backend;
    private DeterministicScheduler scheduler;

    public ChatTranslationTest() {
        super(LiveShowActivity.class);
    }

    public void testAtStartup_RequestsChatContent() throws Exception {
        ChatTranslationRunner app = openScreen();
        backend.hasReceivedInitialRequest();
        backend.respondWithChatStream(chatStream(withMessages("Lorem ipsum", "Dolor sit amet")));

        app.showsChatMessages("Lorem ipsum", "Dolor sit amet");
    }

    public void testRequestNextMessagesWhenRefreshing() throws Exception {
        ChatTranslationRunner app = openScreen();

        backend.hasReceivedInitialRequest();
        backend.respondWithChatStream(chatStream(withMessages("Lorem ipsum")));
        app.showsChatMessages("Lorem ipsum");

        app.refreshChat();
        backend.hasReceivedContinuationRequest();
        backend.respondWithChatStream(chatStream(withMessages("Dolor sit amet")));
        app.showsChatMessages(
                "Lorem ipsum",
                "Dolor sit amet");

        app.refreshChat();
        backend.hasReceivedContinuationRequest();
        backend.respondWithChatStream(chatStream(withMessages("Consectetur adipiscing elit")));
        app.showsChatMessages(
                "Lorem ipsum",
                "Dolor sit amet",
                "Consectetur adipiscing elit");
    }

    public void testDisplayingErrorWhenUnableToGetMessages() throws Exception {
        ChatTranslationRunner app = openScreen();

        backend.hasReceivedInitialRequest();
        backend.respondWithError();
        app.showsErrorMessage();
    }

    private ChatTranslationRunner openScreen() {
        return new ChatTranslationRunner(getInstrumentation(), getActivity(), scheduler);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        backend = new LiveChatTranslationServer();
        scheduler = new DeterministicScheduler();
        ChatTranslationFragment.chatFactory = new ChatTranslation.Factory() {
            @Override
            public ChatTranslation create() {
                return new HttpChatTranslation(
                        LiveChatTranslationServer.baseUrl(), scheduler);
            }
        };
    }

    @Override
    public void tearDown() throws Exception {
        backend.stop();
        super.tearDown();
    }
}

