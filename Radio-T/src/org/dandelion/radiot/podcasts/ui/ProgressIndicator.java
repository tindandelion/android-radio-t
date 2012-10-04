package org.dandelion.radiot.podcasts.ui;

import android.app.Activity;
import android.content.Context;
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
            return new ActionItemIndicator(activity);
        } else {
            return new SimpleProgressIndicator(activity);
        }
    }

    public abstract void setActionItem(MenuItem item);

    private static class SimpleProgressIndicator extends ProgressIndicator {
        private View indicator;

        public SimpleProgressIndicator(Activity activity) {
            super(activity);
            this.indicator = activity.findViewById(R.id.titlebar_progress);
        }

        @Override
        public void setActionItem(MenuItem item) {
        }

        @Override
        public void onStarted() {
            indicator.setVisibility(View.VISIBLE);
        }

        @Override
        public void onFinished() {
            indicator.setVisibility(View.INVISIBLE);
        }
    }

    private static class ActionItemIndicator extends ProgressIndicator {
        private boolean inProgress = false;
        private MenuItem refreshItem;
        private final View progressView;

        public ActionItemIndicator(Activity context) {
            super(context);
            LayoutInflater inflater = context.getLayoutInflater();
            progressView = inflater.inflate(R.layout.progress_view, null);
        }

        @Override
        public void setActionItem(MenuItem item) {
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
