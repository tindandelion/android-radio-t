package org.dandelion.radiot.common.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import org.dandelion.radiot.home_screen.HomeScreenActivity;

public class CustomTitleActivity extends TrackedActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)  {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return handleHome(item) || super.onOptionsItemSelected(item);
    }

    private boolean handleHome(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startHomeActivity();
            return true;
        } else {
            return false;
        }
    }

    private void startHomeActivity() {
        Intent intent = new Intent(this, HomeScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}

