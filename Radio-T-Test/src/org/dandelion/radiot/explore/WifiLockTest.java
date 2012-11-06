package org.dandelion.radiot.explore;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.test.InstrumentationTestCase;

public class WifiLockTest extends InstrumentationTestCase {

    public void testLockWithUnsupportedType_ShouldNotCauseErrors() throws Exception {
        final int UNSUPPORTED_TYPE = 10;
        WifiManager.WifiLock lock = createWifiLockWithType(UNSUPPORTED_TYPE);

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
