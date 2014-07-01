package org.dandelion.radiot.endtoend.live;

import org.dandelion.radiot.endtoend.live.helpers.CurrentTopicRunner;
import org.dandelion.radiot.helpers.ResponsiveHttpServer;
import org.dandelion.radiot.http.DataEngine;
import org.dandelion.radiot.http.HttpDataEngine;
import org.dandelion.radiot.live.schedule.DeterministicScheduler;
import org.dandelion.radiot.live.topics.HttpTopicProvider;
import org.dandelion.radiot.live.ui.CurrentTopicFragment;

import java.io.IOException;

public class CurrentTopicTest extends LiveShowActivityTestCase {
    public static final String DEFAULT_TOPIC = "What is a Web Framework?";
    private TopicTrackerBackend backend;
    private DeterministicScheduler scheduler;

    public void testWhenNoCurrentTopic_hidesTheView() throws Exception {
        backend.respondNoContent();

        CurrentTopicRunner app = openScreen();

        app.showsNoTopic();
    }

    public void testShowsCurrentTopic() throws Exception {
        backend.respondWithTopic(DEFAULT_TOPIC);

        CurrentTopicRunner app = openScreen();

        app.showsCurrentTopic(DEFAULT_TOPIC);
    }


    public void testWhenTopicChanges_refreshView() throws Exception {
        final String newTopic = "Amazon's ginormous public cloud turns 81 today";

        CurrentTopicRunner app = openScreen();

        backend.respondWithTopic(newTopic);
        app.refreshTopic();
        app.showsCurrentTopic(newTopic);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        backend = new TopicTrackerBackend();
        scheduler = new DeterministicScheduler();


        CurrentTopicFragment.trackerFactory = new DataEngine.Factory() {
            @Override
            public DataEngine create() {
                HttpTopicProvider trackerClient = new HttpTopicProvider(TopicTrackerBackend.baseUrl());
                return new HttpDataEngine(trackerClient, scheduler);
            }
        };
    }

    private CurrentTopicRunner openScreen() {
        return new CurrentTopicRunner(getInstrumentation(), getActivity(), scheduler);
    }

    @Override
    public void tearDown() throws Exception {
        backend.stop();
        super.tearDown();
    }

}

class TopicTrackerBackend extends ResponsiveHttpServer {
    private static final String HTTP_NO_CONTENT = "204 No Content";

    public TopicTrackerBackend() throws IOException {
        super();
    }

    public void respondWithTopic(String topicText) {
        respondSuccessWith(String.format("{text:\"%s\"}", topicText), MIME_JSON);
    }

    public void respondNoContent() {
        respondWith(new Response(HTTP_NO_CONTENT, MIME_JSON, ""));
    }
}
