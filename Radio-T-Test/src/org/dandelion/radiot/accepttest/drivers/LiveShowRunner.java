package org.dandelion.radiot.accepttest.drivers;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import org.dandelion.radiot.accepttest.testables.FakeNotificationBar;
import org.dandelion.radiot.live.LiveShowApp;
import org.dandelion.radiot.live.service.LiveShowService;

public class LiveShowRunner {
    private final LiveShowDriver driver;
    private final Context context;
    private final FakeNotificationBar notificationBar;

    public LiveShowRunner(Instrumentation inst, Activity activity, FakeNotificationBar notificationBar) {
        this.driver = new LiveShowDriver(inst, activity);
        this.context = inst.getTargetContext();
        this.notificationBar = notificationBar;
    }

    public void finish() {
        driver.finishOpenedActivities();
        stopService();
    }

    private void stopService() {
        Intent intent = new Intent(context, LiveShowService.class);
        context.stopService(intent);
    }

    public void startTranslation() {
        driver.clickConnect();
    }

    public void showsTranslationInProgress() {
        driver.showTranslationStatus("Трансляция");
        notificationBar.showsIcon(
                LiveShowApp.LIVE_NOTIFICATION_ID, LiveShowApp.LIVE_ICON_RESOURCE_ID,
                "Радио-Т", "Прямой эфир: Идет трансляция");
    }

    public void stopTranslation() {
        driver.clickStop();
    }

    public void showsTranslationStopped() {
        driver.showTranslationStatus("Остановлено");
        notificationBar.hasRemovedIcon(LiveShowApp.LIVE_NOTIFICATION_ID);
    }

    public void showsWaiting() {
        driver.showTranslationStatus("Ожидание");
    }

    public void showsStopped() {
        driver.showTranslationStatus("Остановлено");
    }
}
