package org.dandelion.radiot.accepttest.drivers;

import android.app.Activity;
import android.app.Instrumentation;
import com.jayway.android.robotium.solo.Solo;

import static junit.framework.Assert.assertTrue;

public class LiveShowDriver extends Solo {
    public LiveShowDriver(Instrumentation instrumentation, Activity activity) {
        super(instrumentation, activity);
    }

    public void startTranslation() {
        clickOnButton("Подключиться");
    }

    public void assertShowsTranslation() {
        assertTrue(waitForText("Трансляция"));
    }

    public void stopTranslation() {
        clickOnButton("Остановить");
    }

    public void assertShowsStopped() {
        assertTrue(waitForText("Остановлено"));
    }

    public void assertShowsWaiting() {
        assertTrue(waitForText("Ожидание"));
    }
}
