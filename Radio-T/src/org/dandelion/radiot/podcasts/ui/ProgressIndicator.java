package org.dandelion.radiot.podcasts.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import org.dandelion.radiot.R;
import org.dandelion.radiot.podcasts.core.ProgressListener;
import org.dandelion.radiot.util.ActivityHelper;

abstract class ProgressIndicator extends DialogErrorDisplayer implements ProgressListener {
    public ProgressIndicator(Context context) {
        super(context);
    }

    public static ProgressIndicator create(Activity activity) {
        if (ActivityHelper.supportsActionBar()) {
            return new ActionBarProgressIndicator(activity);
        } else {
            return new DialogProgressIndicator(activity);
        }
    }

    public abstract void setRefreshItem(MenuItem item);

    private static class DialogProgressIndicator extends ProgressIndicator {
        private Activity activity;
        private ProgressDialog progress;

        DialogProgressIndicator(Activity activity) {
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

        @Override
        public void setRefreshItem(MenuItem item) {

        }
    }

    private static class ActionBarProgressIndicator extends ProgressIndicator {
        private boolean inProgress = false;
        private MenuItem refreshItem;
        private final View progressView;

        public ActionBarProgressIndicator(Activity context) {
            super(context);
            LayoutInflater inflater = context.getLayoutInflater();
            progressView = inflater.inflate(R.layout.progress_view, null);
        }

        @Override
        public void setRefreshItem(MenuItem item) {
            this.refreshItem = item;
            updateItem();
        }

        @Override
        public void onStarted() {
            inProgress = true;
            updateItem();
        }

        @Override
        public void onFinished() {
            inProgress = false;
            updateItem();
        }

        private void updateItem() {
            if (refreshItem == null) {
                return;
            }

            if (inProgress) {
                refreshItem.setActionView(progressView);
            } else {
                refreshItem.setActionView(null);
            }
        }

    }
}
