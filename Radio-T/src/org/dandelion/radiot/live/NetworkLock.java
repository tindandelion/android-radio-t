package org.dandelion.radiot.live;

import android.net.wifi.WifiManager;
import org.dandelion.radiot.live.service.Lockable;

public class NetworkLock implements Lockable {

    private WifiManager.WifiLock lock;

    public static NetworkLock create(WifiManager.WifiLock wifiLock) {
        return new NetworkLock(wifiLock);
    }

    private NetworkLock(WifiManager.WifiLock lock) {
        this.lock = lock;
    }

    @Override
    public void release() {
        lock.release();
    }

    @Override
    public void acquire() {
        lock.acquire();
    }
}
