package org.dandelion.radiot.live;

import android.net.wifi.WifiManager;
import org.dandelion.radiot.live.core.states.*;
import org.dandelion.radiot.live.service.WifiLocker;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class WifiLockerTests {
    private WifiManager.WifiLock lock = mock(WifiManager.WifiLock.class);
    private WifiLocker locker = new WifiLocker(lock);

    @Test
    public void locksWifiOnConnecting() throws Exception {
        locker.onConnecting(new Connecting(null));
        verify(lock).acquire();
    }

    @Test
    public void unlocksWifiOnIdle() throws Exception {
        locker.onIdle(new Idle(null));
        verify(lock).release();
    }

    @Test
    public void unlocksWifiOnWaiting() throws Exception {
        locker.onWaiting(new Waiting(null));
        verify(lock).release();
    }
}
