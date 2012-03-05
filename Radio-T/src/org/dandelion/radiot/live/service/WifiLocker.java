package org.dandelion.radiot.live.service;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import org.dandelion.radiot.live.core.LiveShowPlayer;
import org.dandelion.radiot.live.core.states.*;

public class WifiLocker implements LiveShowPlayer.StateVisitor {
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

    @Override
    public void onWaiting(Waiting state) {
        lock.release();
    }

    @Override
    public void onIdle(Idle state) {
        lock.release();
    }

    @Override
    public void onConnecting(Connecting connecting) {
        lock.acquire();
    }

    @Override
    public void onPlaying(Playing playing) {
    }

    @Override
    public void onStopping(Stopping stopping) {
    }
}
