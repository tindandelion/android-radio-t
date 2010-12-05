package org.dandelion.radiot;

import android.app.Activity;
import android.os.Bundle;

public class RadiotActivity extends Activity {

	protected int getContentViewId() {
		return 0;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getContentViewId());
	}
}