package org.dandelion.radiot.explore;

import android.content.Context;
import android.os.PowerManager;
import android.test.InstrumentationTestCase;

public class WakeLockTest extends InstrumentationTestCase {
    public void testDisableReferenceCounting() throws Exception {
        PowerManager.WakeLock lock = createLock();
        lock.setReferenceCounted(false);

        lock.acquire();
        lock.acquire();

        lock.release();
        assertFalse(lock.isHeld());
    }

    public void testUnacquiredNonCountedLock_ShouldReleaseFine() throws Exception {
        PowerManager.WakeLock lock = createLock();
        lock.setReferenceCounted(false);

        lock.release();
        assertFalse(lock.isHeld());
    }

    private PowerManager.WakeLock createLock() {
        Context context = getInstrumentation().getContext();
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WakeLock");
    }
}
