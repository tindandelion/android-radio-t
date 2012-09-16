package org.dandelion.radiot.accepttest.drivers;

import android.app.Activity;
import android.app.Instrumentation;
import com.jayway.android.robotium.solo.Solo;

import static junit.framework.Assert.assertTrue;

// TODO: Merge this functionality into the LiveShowRunner?
public class LiveShowDriver extends Solo {
    public LiveShowDriver(Instrumentation instrumentation, Activity activity) {
        super(instrumentation, activity);
    }

    public void clickConnect() {
        clickOnButton("Подключиться");
    }

    public void showsTranslationStatus(String statusText) {
        assertTrue(waitForText(statusText));
    }

    public void clickStop() {
        clickOnButton("Остановить");
    }
}
