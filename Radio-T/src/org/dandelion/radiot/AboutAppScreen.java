package org.dandelion.radiot;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AboutAppScreen extends Activity {
	private static final String FEEDBACK_EMAIL = "s.moshnikov@gmail.com";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_app);
		updateVerionLabel();
	}

	private void updateVerionLabel() {
		TextView versionView = (TextView) findViewById(R.id.version_label);
		String template = getString(R.string.version_label);
		String version = String.format(template, getAppVersion());
		versionView.setText(version);
	}

	private String getAppVersion() {
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			return "";
		}
	}

	public void sendFeedback(View view) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] { FEEDBACK_EMAIL } );
		intent.putExtra(Intent.EXTRA_SUBJECT, composeFeedbackEmailSubject());
		intent.setType("message/rfc822");
		startActivity(Intent.createChooser(intent, null));
	}

	private CharSequence getAppLabel() {
		return getPackageManager().getApplicationLabel(getApplicationInfo());
	}

	private String composeFeedbackEmailSubject() {
		return String.format("%s %s: Feedback", getAppLabel(), getAppVersion());
	}
}
