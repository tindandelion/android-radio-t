package org.dandelion.radiot.integration;

import android.net.wifi.WifiManager;
import android.test.InstrumentationTestCase;
import org.dandelion.radiot.live.NetworkLock;

public class NetworkLockTest extends InstrumentationTestCase {
    public static final String TAG = "TestLock";
    private WifiManager.WifiLock wifiLock;
    private NetworkLock lock;

    public void testAcquireAndReleaseLock() throws Exception {
        lock.acquire();
        assertTrue(wifiLock.isHeld());

        lock.release();
        assertFalse(wifiLock.isHeld());
    }

    public void testReleaseUnaquiredLock_ShouldNotRaiseError() throws Exception {
        assertFalse(wifiLock.isHeld());

        try {
            lock.release();
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

        assertFalse(wifiLock.isHeld());
    }

    public void testMultipleAcquires_ShouldNotAccumulate() throws Exception {
        lock.acquire();
        lock.acquire();

        lock.release();
        assertFalse(wifiLock.isHeld());
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        wifiLock = createWifiLock();
        lock = new NetworkLock(wifiLock);
    }

    private WifiManager.WifiLock createWifiLock() {
        return NetworkLock.createWifiLock(getInstrumentation().getContext(), TAG);
    }
}
