package org.dandelion.radiot.endtoend.live.helpers;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import org.dandelion.radiot.accepttest.testables.LiveNotificationManagerSpy;
import org.dandelion.radiot.live.service.LiveShowService;

public class LiveShowRunner {
    private final LiveShowUiDriver uiDriver;
    private final Context context;
    private final LiveNotificationManagerSpy notificationManager;

    public LiveShowRunner(Instrumentation inst, Activity activity, LiveNotificationManagerSpy notificationManager) {
        this.uiDriver = new LiveShowUiDriver(inst, activity);
        this.context = inst.getTargetContext();
        this.notificationManager = notificationManager;
    }

    public void finish() {
        uiDriver.finishOpenedActivities();
        stopService();
    }

    private void stopService() {
        Intent intent = new Intent(context, LiveShowService.class);
        context.stopService(intent);
    }

    public void startTranslation() {
        uiDriver.togglePlayback();
    }

    public void showsTranslationInProgress() throws InterruptedException {
        uiDriver.showsTranslationStatus("Трансляция");
        notificationManager.showsForegroundNotification("Прямой эфир: Идет трансляция");
    }

    public void stopTranslation() {
        uiDriver.togglePlayback();
    }

    public void showsTranslationStopped() throws InterruptedException {
        uiDriver.showsTranslationStatus("Остановлено");
        notificationManager.notificationsHidden();
    }

    public void showsWaiting() throws InterruptedException {
        uiDriver.showsTranslationStatus("Ожидание");
        notificationManager.showsBackgroundNotification("Прямой эфир: Ожидание");
    }
}
