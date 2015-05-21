package org.dandelion.radiot.common.ui;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import org.dandelion.radiot.R;
import org.dandelion.radiot.home_screen.HomeScreenActivity;

public abstract class ActivityHelper {
    protected AppCompatActivity activity;

    public static boolean supportsActionBar() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static ActivityHelper create(AppCompatActivity activity) {
        if (supportsActionBar()) {
            return new HoneycombHelper(activity);
        } else {
            return new GingerbreadHelper(activity);
        }
    }

    private ActivityHelper(AppCompatActivity activity) {
        this.activity = activity;
    }

    public abstract void onCreate();

    public abstract void onPostCreate();

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startHomeActivity();
            return true;
        } else {
            return false;
        }
    }

    // TODO: Remove direct reference to HomeScreenActivity
    public void startHomeActivity() {
        Intent intent = new Intent(activity, HomeScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }

    private static class GingerbreadHelper extends ActivityHelper {
        public GingerbreadHelper(AppCompatActivity activity) {
            super(activity);
        }

        @Override
        public void onCreate() {
            activity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        }

        @Override
        public void onPostCreate() {
            activity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
            TextView titleText = (TextView) activity.findViewById(R.id.titlebar_title);
            ImageButton homeButton = (ImageButton) activity.findViewById(R.id.titlebar_icon);

            titleText.setText(activity.getTitle());
            homeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startHomeActivity();
                }
            });
        }
    }

    private static class HoneycombHelper extends ActivityHelper {
        public HoneycombHelper(AppCompatActivity activity) {
            super(activity);
        }

        @Override
        public void onCreate() {
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null)  {
                actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        @Override
        public void onPostCreate() {

        }
    }
}
