package org.dandelion.radiot.endtoend.live.helpers;

import android.app.Activity;
import android.app.Instrumentation;
import com.robotium.solo.Solo;

import static junit.framework.Assert.assertTrue;

public class LiveShowUiDriver extends Solo {

    // 60 seconds to wait for the stupid MediaPlayer to detect that the
    // audio stream is not playable (HTTP error 404). Prior to Android 5.0
    // MediaPlayer.prepareAsync() returned instantly, in Android 5.0 it
    // makes several attempts to connect to the stream, so it only returns after
    // 40 seconds or so.
    public static final int TRANSLATION_TIMEOUT = 60_000;

    public LiveShowUiDriver(Instrumentation instrumentation, Activity activity) {
        super(instrumentation, activity);
    }

    public void togglePlayback() {
        clickOnImageButton(0);
    }

    public void showsTranslationStatus(String statusText) {
        assertTrue(waitForText(statusText, 0, TRANSLATION_TIMEOUT));
    }

}
