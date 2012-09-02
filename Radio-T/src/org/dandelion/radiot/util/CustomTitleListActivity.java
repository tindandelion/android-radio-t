package org.dandelion.radiot.util;

import android.app.ListActivity;
import android.os.Bundle;

public abstract class CustomTitleListActivity extends ListActivity {

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
}
