package org.dandelion.radiot.live.ui;

import android.os.Bundle;
import org.dandelion.radiot.R;
import org.dandelion.radiot.common.ui.CustomTitleActivity;

public class LiveShowActivity extends CustomTitleActivity {

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.live_show_screen);
    }
}
