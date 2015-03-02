package org.dandelion.radiot.explore;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.test.InstrumentationTestCase;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class WifiLockTest extends InstrumentationTestCase {

    public void testLockWithHiPerfType_shouldNotCauseErrors() throws Exception {
        final int LOCK_TYPE = WifiManager.WIFI_MODE_FULL_HIGH_PERF;
        WifiManager.WifiLock lock = createWifiLockWithType(LOCK_TYPE);

        assertNotNull(lock);
        try {
            lock.acquire();
            lock.release();
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    private WifiManager.WifiLock createWifiLockWithType(int type) {
        Context context = getInstrumentation().getContext();
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return manager.createWifiLock(type, "TestLock");
    }
}
