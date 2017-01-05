package org.dandelion.radiot.integration;

import android.app.ActivityManager;
import android.content.Context;
import android.test.InstrumentationTestCase;
import org.dandelion.radiot.helpers.async.Probe;
import org.dandelion.radiot.live.LiveShowApp;
import org.dandelion.radiot.live.core.AudioStream;
import org.dandelion.radiot.live.LiveShowClient;
import org.dandelion.radiot.live.service.LiveShowService;
import org.dandelion.radiot.live.service.Lockable;
import org.hamcrest.Description;

import java.io.IOException;
import java.util.List;

import static org.dandelion.radiot.helpers.async.Poller.assertEventually;

public class LiveShowServiceLifecycleTest extends InstrumentationTestCase {
    private FakeAudioStream audioStream = new FakeAudioStream();
    private FakeLock lock = new FakeLock();

    public void testStartStopServiceInStraightLifecycle() throws Exception {
        startPlayback();
        assertServiceIsStarted();
        stopPlayback();
        assertServiceIsStopped();
    }

    public void testManagesWifiLockInStandardLifecycle() throws Exception {
        startPlayback();
        assertEventually(lockAcquired(lock));
        stopPlayback();
        assertEventually(lockReleased(lock));
    }

    public void testReleasesWifiLockInWaitingLifecycle() throws Exception {
        startPlayback();
        audioStream.signalError();
        assertEventually(lockReleased(lock));
        stopPlayback();
        assertEventually(lockReleased(lock));
    }

    public void testStopServiceWhenPlayerGoesWaiting() throws Exception {
        startPlayback();
        assertServiceIsStarted();

        audioStream.signalError();
        assertServiceIsStopped();

        stopPlayback();
        assertServiceIsStopped();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        LiveShowApp.setTestingInstance(createTestingApp());
    }

    private Probe lockAcquired(FakeLock lock) {
        return new WifiLockProbe(lock, true);
    }

    private Probe lockReleased(final FakeLock lock) {
        return new WifiLockProbe(lock, false);
    }

    private LiveShowApp createTestingApp() {
        return new LiveShowApp() {
            @Override
            public AudioStream createAudioStream() {
                return audioStream;
            }

            @Override
            public Lockable createNetworkLock(Context context) {
                return lock;
            }
        };
    }

    private void assertServiceIsStarted() {
        ActivityManager.RunningServiceInfo info = liveServiceInfo();
        assertNotNull("Service is not running", info);
        assertTrue("Service is not foreground", info.foreground);
    }

    private void assertServiceIsStopped() {
        assertNull("Service is still running", liveServiceInfo());
    }

    private void stopPlayback() throws InterruptedException {
        togglePlayback();
    }

    private void startPlayback() throws InterruptedException {
        togglePlayback();
    }

    private void togglePlayback() throws InterruptedException {
        LiveShowClient client = LiveShowApp.getInstance().createClient(context());
        client.togglePlayback();
        Thread.sleep(500);  // Give a service some time to process the request
    }

    private Context context() {
        return getInstrumentation().getTargetContext();
    }

    private ActivityManager.RunningServiceInfo liveServiceInfo() {
        ActivityManager manager = (ActivityManager) context().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> infos = manager.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo info : infos) {
            if (info.service.getClassName().equals(LiveShowService.class.getName())) {
                return info;
            }
        }
        return null;
    }

    private static class FakeLock implements Lockable {
        volatile boolean isAcquired;

        @Override
        public void release() {
            isAcquired = false;
        }

        @Override
        public void acquire() {
            isAcquired = true;
        }
    }

    private static class WifiLockProbe implements Probe {
        private final FakeLock lock;
        private final boolean expectedState;
        private boolean actualState;

        public WifiLockProbe(FakeLock lock, boolean expectedState) {
            this.lock = lock;
            this.expectedState = expectedState;
        }

        @Override
        public boolean isSatisfied() {
            return actualState == expectedState;
        }

        @Override
        public void sample() {
            actualState = lock.isAcquired;
        }

        @Override
        public void describeAcceptanceCriteriaTo(Description d) {
            d.appendText("Wifi lock state should be: ").appendValue(expectedState);
        }

        @Override
        public void describeFailureTo(Description d) {
            d.appendText("Wifi lock state was: ").appendValue(actualState);
        }
    }

    private class FakeAudioStream implements AudioStream {
        private Listener listener;

        @Override
        public void setStateListener(Listener listener) {
            this.listener = listener;
        }

        @Override
        public void play() throws IOException {
            listener.onStarted();
        }

        @Override
        public void stop() {
            listener.onStopped();
        }

        @Override
        public void release() {
        }

        public void signalError() {
            listener.onError();
        }
    }
}
