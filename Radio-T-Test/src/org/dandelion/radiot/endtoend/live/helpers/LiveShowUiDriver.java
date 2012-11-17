package org.dandelion.radiot.endtoend.live.helpers;

import android.app.Activity;
import android.app.Instrumentation;
import com.jayway.android.robotium.solo.Solo;

import static junit.framework.Assert.assertTrue;

public class LiveShowUiDriver extends Solo {
    public LiveShowUiDriver(Instrumentation instrumentation, Activity activity) {
        super(instrumentation, activity);
    }

    public void togglePlayback() {
        clickOnButton(0);
    }

    public void showsTranslationStatus(String statusText) {
        assertTrue(waitForText(statusText));
    }

}
