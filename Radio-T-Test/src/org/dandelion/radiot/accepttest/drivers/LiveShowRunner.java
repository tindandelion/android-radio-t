package org.dandelion.radiot.accepttest.drivers;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import org.dandelion.radiot.accepttest.testables.FakeStatusDisplayer;
import org.dandelion.radiot.live.core.LiveShowState;
import org.dandelion.radiot.live.service.LiveShowService;

public class LiveShowRunner {
    private final LiveShowUiDriver uiDriver;
    private final Context context;
    private final FakeStatusDisplayer statusNotifier;

    public LiveShowRunner(Instrumentation inst, Activity activity, FakeStatusDisplayer statusNotifier) {
        this.uiDriver = new LiveShowUiDriver(inst, activity);
        this.context = inst.getTargetContext();
        this.statusNotifier = statusNotifier;
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
        uiDriver.clickConnect();
    }

    public void showsTranslationInProgress() {
        uiDriver.showsTranslationStatus("Трансляция");
        statusNotifier.showsStatusFor(LiveShowState.Playing);
    }

    public void stopTranslation() {
        uiDriver.clickStop();
    }

    public void showsTranslationStopped() {
        uiDriver.showsTranslationStatus("Остановлено");
        statusNotifier.showsStatusFor(LiveShowState.Idle);
    }

    public void showsWaiting() {
        uiDriver.showsTranslationStatus("Ожидание");
    }

    public void showsStopped() {
        uiDriver.showsTranslationStatus("Остановлено");
    }
}
