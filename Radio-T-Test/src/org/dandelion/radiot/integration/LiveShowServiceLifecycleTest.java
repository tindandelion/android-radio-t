package org.dandelion.radiot.integration;

import android.content.Intent;
import android.test.ServiceTestCase;
import org.dandelion.radiot.live.service.LiveShowService;

public class LiveShowServiceLifecycleTest extends ServiceTestCase<LiveShowService> {
    public LiveShowServiceLifecycleTest() {
        super(LiveShowService.class);
    }

    public void testAllOk() throws Exception {
        assertTrue(true);
    }

    public void testServiceStopsItselfWhenPlaybackIsStopped() throws Exception {
        startPlayback();
        stopPlayback();
        assertServiceIsStopped();
    }

    private void assertServiceIsStopped() {
        fail("Not implemented");
    }

    private void stopPlayback() {
        throw new RuntimeException("Not implemented");
    }

    private void startPlayback() {
        Intent intent = new Intent(LiveShowService.TOGGLE_ACTION);
        startService(intent);
    }
}
