package org.dandelion.radiot.live;

import android.content.Context;
import android.net.wifi.WifiManager;
import org.dandelion.radiot.live.service.Lockable;

public class NetworkLock implements Lockable {
    public static final String TAG = NetworkLock.class.getName();
    private static final String NORMAL_LOCK_TAG = TAG + ".Normal";
    private static final String HIPERF_LOCK_TAG = TAG + ".HiPerf";
    private static final int WIFI_MODE_FULL_HIGH_PERF = 3;

    private WifiManager.WifiLock[] locks;

    public static NetworkLock create(Context context) {
        return new NetworkLock(
                createHiPerfLock(context),
                createNormalLock(context));
    }

    private static WifiManager.WifiLock createHiPerfLock(Context context) {
        return createLock(context, WIFI_MODE_FULL_HIGH_PERF, HIPERF_LOCK_TAG);
    }

    private static WifiManager.WifiLock createNormalLock(Context context) {
        return createLock(context, WifiManager.WIFI_MODE_FULL, NORMAL_LOCK_TAG);
    }

    public static WifiManager.WifiLock createLock(Context context, int type, String tag) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiManager.WifiLock l = wm.createWifiLock(type, tag);
        l.setReferenceCounted(false);
        return l;
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

}
