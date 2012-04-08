package org.dandelion.radiot.live.service;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import org.dandelion.radiot.live.core.LiveShowState;

public class WifiLocker {
    private WifiLock lock;
    
    public static WifiLocker create(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiLock lck = wifiManager.createWifiLock("LiveShow");
        lck.setReferenceCounted(false);
        return new WifiLocker(lck);
    }

    public WifiLocker(WifiLock lock) {
        this.lock = lock;
    }

    public void release() {
        lock.release();
    }

    public void updateLock(LiveShowState state) {
        if (state == LiveShowState.Connecting) {
            lock.acquire();
        } else if (LiveShowState.isInactive(state)) {
            lock.release();
        }
    }
}
