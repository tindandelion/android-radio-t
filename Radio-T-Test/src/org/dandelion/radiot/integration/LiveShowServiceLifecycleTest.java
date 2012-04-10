package org.dandelion.radiot.integration;

import android.app.ActivityManager;
import android.content.Context;
import android.test.InstrumentationTestCase;
import org.dandelion.radiot.live.LiveShowApp;
import org.dandelion.radiot.live.service.LiveShowClient;
import org.dandelion.radiot.live.service.LiveShowService;

import java.util.List;

public class LiveShowServiceLifecycleTest extends InstrumentationTestCase {
    public void testStopServiceWhenPlayerGoesIdle() throws Exception {
        startPlayback();
        assertServiceIsStarted();
        stopPlayback();
        assertServiceIsStopped();
    }

    private void assertServiceIsStarted() {
        assertNotNull("Service is not running", liveServiceInfo());
    }

    private void assertServiceIsStopped() {
        assertNull("Service is still running", liveServiceInfo());
    }

    private void stopPlayback() {
        togglePlayback();
    }

    private void startPlayback() {
        togglePlayback();
    }

    private void togglePlayback() {
        LiveShowClient client = LiveShowApp.getInstance().createClient(context());
        client.togglePlayback();
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
}
