package org.dandelion.radiot.unittest.live;

import android.test.AndroidTestCase;
import org.dandelion.radiot.live.service.AlarmTimeout;

public class AlarmTimeoutTestCase extends AndroidTestCase {

    private AlarmTimeout timeout;
    private int timeoutCount = 0;
    private Runnable onTimeout = new Runnable() {
        @Override
        public void run() {
            timeoutCount += 1;
        }
    };

    protected void setUp() throws Exception {
        super.setUp();
        timeout = new AlarmTimeout(getContext(), "TestAction");
    }

    @Override
    public void tearDown() throws Exception {
        timeout.release();
    }

    public void testSignalsTimeout() throws Exception {
        timeout.set(100, onTimeout);
        Thread.sleep(200);
        assertEquals(1, timeoutCount);
    }

    public void testResettingTimeout() throws Exception {
        timeout.set(100, onTimeout);
        timeout.reset();
        Thread.sleep(200);
        assertEquals(0, timeoutCount);
    }

    public void testSignalsTimeoutOnlyOnce() throws Exception {
        timeout.set(100, onTimeout);
        Thread.sleep(1000);
        assertEquals(1, timeoutCount);
    }
}
