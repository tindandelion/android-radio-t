package org.dandelion.radiot.live.service;

import android.net.wifi.WifiManager;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class WifiLockerTest {
    private WifiManager.WifiLock lock = mock(WifiManager.WifiLock.class);
    private WifiLocker locker = new WifiLocker(lock);
    public static final long TIMESTAMP = 0;

    @Test
    public void locksWifiOnConnecting() throws Exception {
        locker.onConnecting(TIMESTAMP);
        verify(lock).acquire();
    }

    @Test
    public void unlocksWifiOnIdle() throws Exception {
        locker.onIdle();
        verify(lock).release();
    }

    @Test
    public void unlocksWifiOnWaiting() throws Exception {
        locker.onWaiting(TIMESTAMP);
        verify(lock).release();
    }
}
