package org.dandelion.radiot.podcasts.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import org.dandelion.radiot.R;
import org.dandelion.radiot.podcasts.core.ProgressListener;

class ProgressDisplayer extends DialogErrorDisplayer implements ProgressListener {
    private Activity activity;
    private ProgressDialog progress;

    public ProgressDisplayer(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    public void onStarted() {
        progress = ProgressDialog.show(activity, null,
                activity.getString(R.string.loading_message), true, true,
                new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        activity.finish();
                    }
                });
    }

    @Override
    public void onFinished() {
        progress.dismiss();
    }
}
