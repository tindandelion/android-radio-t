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

    private CurrentTopicRunner app;
    private TopicTrackerBackend backend;

    public void testShowsCurrentTopic() throws Exception {
        app.showsCurrentTopic(DEFAULT_TOPIC);
    }


    public void testWhenTopicChanges_refreshView() throws Exception {
        final String newTopic = "Amazon's ginormous public cloud turns 81 today";
        backend.respondWithTopic(newTopic);
        app.refreshTopic();
        app.showsCurrentTopic(newTopic);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        backend = new TopicTrackerBackend();
        backend.respondWithTopic(DEFAULT_TOPIC);

        final HttpTopicProvider trackerClient = new HttpTopicProvider(TopicTrackerBackend.baseUrl());
        final DeterministicScheduler scheduler = new DeterministicScheduler();

        CurrentTopicFragment.trackerFactory = new DataEngine.Factory() {
            @Override
            public DataEngine create() {
                return new HttpDataEngine(trackerClient, scheduler);
            }
        };

        app = new CurrentTopicRunner(getInstrumentation(), getActivity(), scheduler);
    }

    @Override
    public void tearDown() throws Exception {
        backend.stop();
        super.tearDown();
    }

    private static class TopicTrackerBackend extends ResponsiveHttpServer {
        public TopicTrackerBackend() throws IOException {
            super();
        }

        public void respondWithTopic(String topicText) {
            respondSuccessWith(String.format("{text:\"%s\"}", topicText), MIME_JSON);
        }
    }
}
