package org.dandelion.radiot.live;

import android.content.Context;
import android.net.wifi.WifiManager;
import org.dandelion.radiot.live.service.Lockable;

public class NetworkLock implements Lockable {
    private static final String TAG = NetworkLock.class.getName() + ".WifiLock";
    private WifiManager.WifiLock[] locks;

    public static NetworkLock create(Context context) {
        return new NetworkLock(createWifiLock(context, TAG));
    }

    public NetworkLock(WifiManager.WifiLock... locks) {
        this.locks = locks;
    }

    @Override
    public void release() {
        for (WifiManager.WifiLock l : locks) {
            l.release();
        }
    }

    @Override
    public void acquire() {
        for (WifiManager.WifiLock l : locks) {
            l.acquire();
        }
    }

    public static WifiManager.WifiLock createWifiLock(Context context, String tag) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiManager.WifiLock l = wm.createWifiLock(tag);
        l.setReferenceCounted(false);
        return l;
    }
}
