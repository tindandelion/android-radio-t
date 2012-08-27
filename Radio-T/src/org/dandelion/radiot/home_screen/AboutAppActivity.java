package org.dandelion.radiot.home_screen;

import android.os.Bundle;
import org.dandelion.radiot.R;
import org.dandelion.radiot.util.CustomTitleActivity;

public class AboutAppActivity extends CustomTitleActivity {
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.about_app);
	}
}
