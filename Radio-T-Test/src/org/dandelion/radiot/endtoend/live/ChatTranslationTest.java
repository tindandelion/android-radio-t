package org.dandelion.radiot.endtoend.live;

import org.dandelion.radiot.endtoend.live.helpers.ChatTranslationRunner;
import org.dandelion.radiot.endtoend.live.helpers.LiveChatTranslationServer;
import org.dandelion.radiot.http.DataEngine;
import org.dandelion.radiot.live.chat.ChatTranslation;
import org.dandelion.radiot.live.schedule.DeterministicScheduler;
import org.dandelion.radiot.live.ui.ChatTranslationFragment;

import static org.dandelion.radiot.util.ChatStreamBuilder.chatStream;
import static org.dandelion.radiot.util.ChatStreamBuilder.message;

public class ChatTranslationTest extends LiveShowActivityTestCase {
    private LiveChatTranslationServer backend;
    private DeterministicScheduler scheduler;

    public void testAtStartup_RequestsChatContent() throws Exception {
        ChatTranslationRunner app = openScreen();
        backend.hasReceivedInitialRequest();
        backend.respondWithChatStream(chatStream(
                message(1, "Lorem ipsum"),
                message(2, "Dolor sit amet")));

        app.showsChatMessages("Lorem ipsum", "Dolor sit amet");
    }

    public void testRequestNextMessagesWhenRefreshing() throws Exception {
        final int INITIAL_SEQ = 1;
        final ChatTranslationRunner app = openScreen();

        backend.hasReceivedInitialRequest();
        backend.respondWithChatStream(chatStream(message(INITIAL_SEQ, "Lorem ipsum")));
        app.showsChatMessages("Lorem ipsum");

        app.refreshChat();
        backend.hasReceivedContinuationRequest(INITIAL_SEQ);
        backend.respondWithChatStream(chatStream(message(INITIAL_SEQ + 1, "Dolor sit amet")));
        app.showsChatMessages(
                "Lorem ipsum",
                "Dolor sit amet");

        app.refreshChat();
        backend.hasReceivedContinuationRequest(INITIAL_SEQ + 1);
        backend.respondWithChatStream(chatStream(message(INITIAL_SEQ + 2, "Consectetur adipiscing elit")));
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
        ChatTranslationFragment.chatFactory = new DataEngine.Factory() {
            @Override
            public DataEngine create() {
                return new ChatTranslation(
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

