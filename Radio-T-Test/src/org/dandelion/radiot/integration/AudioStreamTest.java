package org.dandelion.radiot.integration;

import android.test.InstrumentationTestCase;
import org.dandelion.radiot.integration.helpers.LiveStreamServer;
import org.dandelion.radiot.integration.helpers.NotificationTrace;
import org.dandelion.radiot.live.MediaPlayerStream;
import org.dandelion.radiot.live.core.AudioStream;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;

public class AudioStreamTest extends InstrumentationTestCase {
    private LiveStreamServer backend;

    public void testWhenStartingPlayback_ShouldRequestStreamFromServer() throws Exception {
        AudioStream stream = new MediaPlayerStream(LiveStreamServer.DIRECT_URL);
        MyListener listener = new MyListener();
        stream.setStateListener(listener);
        startPlayback(stream);
        backend.hasReceivedRequest(equalTo(LiveStreamServer.DIRECT_RESOURCE));
        listener.hasStarted();
    }

    private void startPlayback(final AudioStream stream) {
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                try {
                    stream.play();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        backend = new LiveStreamServer(getInstrumentation().getContext());
    }

    @Override
    public void tearDown() throws Exception {
        backend.stop();
        super.tearDown();
    }

    private static class MyListener implements AudioStream.Listener {
        private NotificationTrace<String> messages = new NotificationTrace<String>(30000);

        @Override
        public void onStarted() {
            messages.append("onStarted");
        }

        @Override
        public void onError() {
        }

        @Override
        public void onStopped() {
        }

        public void hasStarted() throws InterruptedException {
            messages.containsNotification(equalTo("onStarted"));
        }
    }
}
