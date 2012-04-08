package org.dandelion.radiot.accepttest.drivers;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import org.dandelion.radiot.accepttest.testables.FakeStatusDisplayer;
import org.dandelion.radiot.live.core.LiveShowState;
import org.dandelion.radiot.live.service.LiveShowService;

public class LiveShowRunner {
    private final LiveShowDriver driver;
    private final Context context;
    private final FakeStatusDisplayer statusNotifier;

    public LiveShowRunner(Instrumentation inst, Activity activity, FakeStatusDisplayer statusNotifier) {
        this.driver = new LiveShowDriver(inst, activity);
        this.context = inst.getTargetContext();
        this.statusNotifier = statusNotifier;
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
        statusNotifier.showsStatusFor(LiveShowState.Playing);
    }

    public void stopTranslation() {
        driver.clickStop();
    }

    public void showsTranslationStopped() {
        driver.showTranslationStatus("Остановлено");
        statusNotifier.showsStatusFor(LiveShowState.Idle);
    }

    public void showsWaiting() {
        driver.showTranslationStatus("Ожидание");
    }

    public void showsStopped() {
        driver.showTranslationStatus("Остановлено");
    }
}
