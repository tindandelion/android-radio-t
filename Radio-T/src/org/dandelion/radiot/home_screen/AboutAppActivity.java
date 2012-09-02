package org.dandelion.radiot.home_screen;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import org.dandelion.radiot.R;
import org.dandelion.radiot.util.AppInfo;
import org.dandelion.radiot.util.CustomTitleActivity;
import org.dandelion.radiot.util.FeedbackEmail;

public class AboutAppActivity extends CustomTitleActivity {

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.about_app);
		setVersionLabel(appInfo().getVersion());
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
