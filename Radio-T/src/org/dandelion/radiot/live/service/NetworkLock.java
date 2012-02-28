package org.dandelion.radiot.live.service;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;

public class NetworkLock {
    private WifiLock lock;

    public NetworkLock(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        lock = wifiManager.createWifiLock("LiveShow");
        lock.setReferenceCounted(false);
    }

    public void acquire() {
        lock.acquire();
    }
    
    public void release() {
        lock.release();
    }

}
