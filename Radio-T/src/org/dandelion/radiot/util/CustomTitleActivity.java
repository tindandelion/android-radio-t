package org.dandelion.radiot.util;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.view.MenuItem;
import org.dandelion.radiot.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import org.dandelion.radiot.home_screen.HomeScreenActivity;

public abstract class CustomTitleActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            onCreateGingerbread();
        } else {
            onCreateHoneycomb();
        }
    }

    private void onCreateHoneycomb() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void onCreateGingerbread() {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            onPostCreateGingerbread();
        } else {
            onPostCreateHoneycomb();
        }
    }

    private void onPostCreateGingerbread() {
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
        TextView titleText = (TextView) findViewById(R.id.titlebar_title);
        titleText.setText(getTitle());
    }

    private void onPostCreateHoneycomb() {

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
