package org.dandelion.radiot.util;

import android.content.res.TypedArray;
import android.util.Log;
import org.dandelion.radiot.R;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public abstract class CustomTitleListActivity extends ListActivity {
    private boolean useCustomTitle = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (shouldUseCustomTitle()) {
            useCustomTitle = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        }
    }

    protected boolean shouldUseCustomTitle() {
        return false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (useCustomTitle) {
            setupTitleBar();
        }
    }

    private void setupTitleBar() {
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
        TextView titleText = (TextView) findViewById(R.id.titlebar_title);
        titleText.setText(getTitle());
    }

}
