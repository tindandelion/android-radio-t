package org.dandelion.radiot.podcasts.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import org.dandelion.radiot.R;
import org.dandelion.radiot.podcasts.loader.ProgressListener;
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

    public abstract void onPostCreate(Activity activity);
    public abstract void onCreateOptionsMenu(Menu menu);


    private static class SimpleProgressIndicator extends ProgressIndicator {
        private View indicator;

        public SimpleProgressIndicator(Activity activity) {
            super(activity);
        }

        @Override
        public void onPostCreate(Activity activity) {
            this.indicator = activity.findViewById(R.id.titlebar_progress);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu) {

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
        private MenuItem menuItem;
        private View progressView;

        public ActionItemIndicator(Activity context) {
            super(context);
        }

        @Override
        public void onPostCreate(Activity activity) {
            LayoutInflater inflater = activity.getLayoutInflater();
            progressView = inflater.inflate(R.layout.progress_view, null);
            updateProgress();
        }

        @Override
        public void onCreateOptionsMenu(Menu menu) {
            menuItem = menu.findItem(R.id.refresh);
            updateProgress();
        }

        @Override
        public void onStarted() {
            inProgress = true;
            updateProgress();
        }

        @Override
        public void onFinished() {
            inProgress = false;
            updateProgress();
        }

        private void updateProgress() {
            if (!isConstructed()) {
                return;
            }

            if (inProgress) {
                menuItem.setActionView(progressView);
            } else {
                menuItem.setActionView(null);
            }
        }

        private boolean isConstructed() {
            return (menuItem != null) && (progressView != null);
        }

    }
}
