package org.dandelion.radiot.integration;

import android.net.wifi.WifiManager;
import android.test.InstrumentationTestCase;
import org.dandelion.radiot.live.NetworkLock;

public class NetworkLockTest extends InstrumentationTestCase {
    private WifiManager.WifiLock wifiLock;
    private NetworkLock lock;

    public void testAcquireAndReleaseLock() throws Exception {
        lock.acquire();
        assertLocksAquired(wifiLock);

        lock.release();
        assertLocksReleased(wifiLock);
    }

    public void testReleaseUnaquiredLock_ShouldNotRaiseError() throws Exception {
        assertLocksReleased(wifiLock);

        try {
            lock.release();
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

        assertLocksReleased(wifiLock);
    }

    public void testMultipleAcquires_ShouldNotAccumulate() throws Exception {
        lock.acquire();
        lock.acquire();

        lock.release();
        assertLocksReleased(wifiLock);
    }

    public void testAcquireAndReleaseMultipleLocks() throws Exception {
        WifiManager.WifiLock firstLock = createWifiLock();
        WifiManager.WifiLock anotherLock = createWifiLock();
        NetworkLock multiLock = new NetworkLock(firstLock, anotherLock);

        multiLock.acquire();
        assertLocksAquired(firstLock, anotherLock);

        multiLock.release();
        assertLocksReleased(firstLock, anotherLock);
    }

    private void assertLocksAquired(WifiManager.WifiLock... locks) {
        for (WifiManager.WifiLock l : locks) {
            assertTrue(l.isHeld());
        }
    }

    private void assertLocksReleased(WifiManager.WifiLock... locks) {
        for (WifiManager.WifiLock l : locks) {
            assertFalse(l.isHeld());
        }
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        wifiLock = createWifiLock();
        lock = new NetworkLock(wifiLock);
    }

    private WifiManager.WifiLock createWifiLock() {
        return NetworkLock.createLock(getInstrumentation().getContext(),
                WifiManager.WIFI_MODE_FULL, "TestLock");
    }
}
