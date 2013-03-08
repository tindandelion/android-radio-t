package org.dandelion.radiot.endtoend.live;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import org.dandelion.radiot.accepttest.testables.FakeStatusDisplayer;
import org.dandelion.radiot.endtoend.live.helpers.LiveShowRunner;
import org.dandelion.radiot.endtoend.live.helpers.LiveShowServer;
import org.dandelion.radiot.live.LiveShowApp;
import org.dandelion.radiot.live.MediaPlayerStream;
import org.dandelion.radiot.live.chat.ProgressListener;
import org.dandelion.radiot.live.chat.MessageConsumer;
import org.dandelion.radiot.live.core.AudioStream;
import org.dandelion.radiot.live.core.LiveShowStateListener;
import org.dandelion.radiot.live.service.TimeoutReceiver;
import org.dandelion.radiot.live.chat.ChatTranslation;
import org.dandelion.radiot.live.ui.ChatTranslationFragment;
import org.dandelion.radiot.live.ui.LiveShowActivity;

public class LiveShowPlaybackTest extends
        ActivityInstrumentationTestCase2<LiveShowActivity> {
    private LiveShowRunner runner;
    private LiveShowServer backend;

    public void testStartStopPlayback() throws Exception {
        backend.activateTranslation();

        runner.startTranslation();
        runner.showsTranslationInProgress();

        runner.stopTranslation();
        runner.showsTranslationStopped();

        runner.startTranslation();
        runner.showsTranslationInProgress();
    }

    public void testTryToReconnectContinuouslyInWaitingMode() throws Exception {
        backend.suppressTranslation();
        runner.startTranslation();
        runner.showsWaiting();

        backend.activateTranslation();
        signalWaitTimeout();
        runner.showsTranslationInProgress();
    }

    public void testStopWaiting() throws Exception {
        backend.suppressTranslation();

        runner.startTranslation();
        runner.showsWaiting();

        runner.stopTranslation();
        runner.showsStopped();
    }

    public LiveShowPlaybackTest() {
        super(LiveShowActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        FakeStatusDisplayer statusDisplayer = new FakeStatusDisplayer();
        setupTestingApp(statusDisplayer);

        ChatTranslationFragment.chatFactory = new NullChatTranlation();
        runner = new LiveShowRunner(getInstrumentation(), getActivity(), statusDisplayer);
        backend = new LiveShowServer(getInstrumentation().getContext());
    }

    @Override
    protected void tearDown() throws Exception {
        runner.finish();
        backend.stop();
        super.tearDown();
    }

    private void setupTestingApp(final LiveShowStateListener statusDisplayer) {
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                LiveShowApp.setTestingInstance(new LiveShowApp() {
                    @Override
                    public AudioStream createAudioStream() {
                        return new MediaPlayerStream(LiveShowServer.SHOW_URL);
                    }

                    @Override
                    public LiveShowStateListener createStatusDisplayer(Context context) {
                        return statusDisplayer;
                    }
                });
            }
        });
    }

    private void signalWaitTimeout() {
        Intent intent = new Intent(TimeoutReceiver.BROADCAST);
        cancelScheduledAlarm(intent);
        fireSimulatedAlarm(intent);
    }

    private void cancelScheduledAlarm(Intent intent) {
        AlarmManager manager = (AlarmManager) context().getSystemService(Context.ALARM_SERVICE);
        manager.cancel(PendingIntent.getBroadcast(context(), 0, intent, 0));
    }

    private void fireSimulatedAlarm(Intent intent) {
        context().sendBroadcast(intent);
    }

    private Context context() {
        return getInstrumentation().getTargetContext().getApplicationContext();
    }

    private static class NullChatTranlation implements ChatTranslation, ChatTranslation.Factory {
        @Override
        public void setProgressListener(ProgressListener listener) {
        }

        @Override
        public void setMessageConsumer(MessageConsumer consumer) {
        }

        @Override
        public void start() {
        }

        @Override
        public void stop() {
        }

        @Override
        public void shutdown() {
        }

        @Override
        public ChatTranslation create() {
            return this;
        }
    }
}
