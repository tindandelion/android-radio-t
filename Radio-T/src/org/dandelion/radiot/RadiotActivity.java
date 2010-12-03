package org.dandelion.radiot;

import org.dandelion.radiot.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class RadiotActivity extends Activity {

	protected int getContentViewId() {
		return 0;
	}

	protected String getTitleString() {
		return getString(getTitleId());
	}

	protected int getTitleId() {
		return 0;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(getContentViewId());
		setCustomTitle(getTitleString());
	}
	
	private void setCustomTitle(String title) {
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.window_title);
		TextView windowTitle = (TextView) findViewById(R.id.window_title_text);
		windowTitle.setText(title);
	}
}