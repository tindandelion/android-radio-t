package org.dandelion.radiot.endtoend.live.helpers;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageButton;
import com.robotium.solo.Condition;
import com.robotium.solo.Solo;
import org.dandelion.radiot.accepttest.testables.LiveNotificationManagerSpy;
import org.dandelion.radiot.live.service.LiveShowService;

import static junit.framework.Assert.assertTrue;

public class LiveShowRunner {

    // 60 seconds to wait for the stupid MediaPlayer to detect that the
    // audio stream is not playable (HTTP error 404). Prior to Android 5.0
    // MediaPlayer.prepareAsync() returned instantly, in Android 5.0 it
    // makes several attempts to connect to the stream, so it only returns after
    // 40 seconds or so.
    private static final int TRANSLATION_TIMEOUT = 60_000;

    private final Solo solo;
    private final Context context;
    private final LiveNotificationManagerSpy notificationManager;

    public LiveShowRunner(Instrumentation inst, Activity activity, LiveNotificationManagerSpy notificationManager) {
        this.solo = new Solo(inst, activity);
        this.context = inst.getTargetContext();
        this.notificationManager = notificationManager;
    }

    public void finish() {
        solo.finishOpenedActivities();
        stopService();
    }

    private void stopService() {
        Intent intent = new Intent(context, LiveShowService.class);
        context.stopService(intent);
    }

    public void startTranslation() {
        togglePlayback();
    }

    public void showsTranslationInProgress() throws InterruptedException {
        showsTranslationStatus("Трансляция");
        notificationManager.showsForegroundNotification("Прямой эфир: Идет трансляция");
    }

    public void stopTranslation() {
        togglePlayback();
    }

    public void showsTranslationStopped() throws InterruptedException {
        showsTranslationStatus("Остановлено");
        notificationManager.notificationsHidden();
    }

    public void showsWaiting() throws InterruptedException {
        showsTranslationStatus("Ожидание");
        notificationManager.showsBackgroundNotification("Прямой эфир: Ожидание");
    }

    private void showsTranslationStatus(String text) {
        assertTrue(solo.waitForText(text, 0, TRANSLATION_TIMEOUT));
    }

    private void togglePlayback() {
        int btnIndex = waitForTogglePlaybackButton();
        solo.clickOnImageButton(btnIndex);
    }

    private int waitForTogglePlaybackButton() {
        final int expectedBtnIndex = 0;
        solo.waitForCondition(new Condition() {
            @Override
            public boolean isSatisfied() {
                ImageButton imageButton = solo.getImageButton(expectedBtnIndex);
                return imageButton.getId() == org.dandelion.radiot.R.id.btn_toggle_live_playback;
            }
        }, smallTimeout());
        return expectedBtnIndex;
    }

    private int smallTimeout() {
        return solo.getConfig().timeout_small;
    }
}
