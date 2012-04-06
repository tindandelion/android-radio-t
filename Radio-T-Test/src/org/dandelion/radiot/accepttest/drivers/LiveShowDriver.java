package org.dandelion.radiot.accepttest.drivers;

import android.app.Activity;
import android.app.Instrumentation;
import com.jayway.android.robotium.solo.Solo;

import static junit.framework.Assert.assertTrue;

public class LiveShowDriver extends Solo {
    public LiveShowDriver(Instrumentation instrumentation, Activity activity) {
        super(instrumentation, activity);
    }

    public void clickConnect() {
        clickOnButton("Подключиться");
    }

    public void showsTranslatingStatus() {
        assertTrue(waitForText("Трансляция"));
    }

    public void clickStop() {
        clickOnButton("Остановить");
    }

    public void showsStoppedStatus() {
        assertTrue(waitForText("Остановлено"));
    }

    public void showsWaitingStatus() {
        assertTrue(waitForText("Ожидание"));
    }
}
