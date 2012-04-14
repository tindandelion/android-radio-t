package org.dandelion.radiot.home_screen;

import android.view.Window;
import org.dandelion.radiot.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import org.dandelion.radiot.util.AppInfo;
import org.dandelion.radiot.util.FeedbackEmail;

public class AboutAppActivity extends Activity {

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.about_app);
		setVersionLabel(appInfo().getVersion());
	}

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupTitleBar();
    }

    private void setupTitleBar() {
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
        TextView titleText = (TextView) findViewById(R.id.titlebar_title);
        titleText.setText(getTitle());
        titleText.setCompoundDrawablesWithIntrinsicBounds(getTitleIcon(), 0, 0, 0);
    }

    private int getTitleIcon() {
        return R.drawable.ic_title_about;
    }

    @SuppressWarnings("UnusedParameters")
    public void sendFeedback(View view) {
        new FeedbackEmail(this, appInfo()).openInEditor();
	}

    private void setVersionLabel(String value) {
        TextView versionView = (TextView) findViewById(R.id.version_label);
        String template = getString(R.string.version_label);
        String version = String.format(template, value);
        versionView.setText(version);
    }

    private AppInfo appInfo() {
        return new AppInfo(this);
    }
}
