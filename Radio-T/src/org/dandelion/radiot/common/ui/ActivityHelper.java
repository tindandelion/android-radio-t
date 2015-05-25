package org.dandelion.radiot.common.ui;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import org.dandelion.radiot.home_screen.HomeScreenActivity;

public class ActivityHelper {
    protected AppCompatActivity activity;

    public static ActivityHelper create(AppCompatActivity activity) {
        return new ActivityHelper(activity);
    }

    private ActivityHelper(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void onCreate() {
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null)  {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

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
}
