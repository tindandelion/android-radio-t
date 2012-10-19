package org.dandelion.radiot.live;

import android.content.Context;
import android.net.wifi.WifiManager;
import org.dandelion.radiot.live.service.Lockable;

public class NetworkLock implements Lockable {
    private static final String TAG = NetworkLock.class.getName() + ".WifiLock";
    private WifiManager.WifiLock lock;

    public static NetworkLock create(Context context) {
        return new NetworkLock(createWifiLock(context, TAG));
    }

    public NetworkLock(WifiManager.WifiLock lock) {
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

    public static WifiManager.WifiLock createWifiLock(Context context, String tag) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiManager.WifiLock l = wm.createWifiLock(tag);
        l.setReferenceCounted(false);
        return l;
    }
}
