package org.dandelion.radiot.accepttest.drivers;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import org.dandelion.radiot.live.service.LiveShowService;

public class LiveShowRunner {
    private LiveShowDriver driver;
    private Context context;

    public LiveShowRunner(Instrumentation inst, Activity activity) {
        this.driver = new LiveShowDriver(inst, activity);
        this.context = inst.getTargetContext();
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
        driver.showsTranslatingStatus();
    }

    public void stopTranslation() {
        driver.clickStop();
    }

    public void showsTranslationStopped() {
        driver.showsStoppedStatus();
    }

    public void showsWaiting() {
        driver.showsWaitingStatus();
    }

    public void showsStopped() {
        driver.showsStoppedStatus();
    }
}
