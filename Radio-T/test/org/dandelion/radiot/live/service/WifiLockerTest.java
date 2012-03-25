package org.dandelion.radiot.live.service;

import android.net.wifi.WifiManager;
import org.dandelion.radiot.live.core.states.Connecting;
import org.dandelion.radiot.live.core.states.Idle;
import org.dandelion.radiot.live.core.states.Waiting;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class WifiLockerTest {
    private WifiManager.WifiLock lock = mock(WifiManager.WifiLock.class);
    private WifiLocker locker = new WifiLocker(lock);

    @Test
    public void locksWifiOnConnecting() throws Exception {
        locker.onConnecting(new Connecting());
        verify(lock).acquire();
    }

    @Test
    public void unlocksWifiOnIdle() throws Exception {
        locker.onIdle(new Idle());
        verify(lock).release();
    }

    @Test
    public void unlocksWifiOnWaiting() throws Exception {
        locker.onWaiting(new Waiting());
        verify(lock).release();
    }
}
