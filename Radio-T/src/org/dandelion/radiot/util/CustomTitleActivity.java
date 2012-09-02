package org.dandelion.radiot.util;

import android.content.Intent;
import android.view.MenuItem;
import android.app.Activity;
import android.os.Bundle;
import org.dandelion.radiot.home_screen.HomeScreenActivity;

public class CustomTitleActivity extends Activity {
    private ActivityHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = ActivityHelper.create(this);
        helper.onCreate();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        helper.onPostCreate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, HomeScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

