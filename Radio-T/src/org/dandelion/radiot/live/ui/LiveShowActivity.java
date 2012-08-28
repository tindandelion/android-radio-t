package org.dandelion.radiot.live.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import org.dandelion.radiot.R;
import org.dandelion.radiot.home_screen.HomeScreenActivity;
import org.dandelion.radiot.util.CustomTitleActivity;

public class LiveShowActivity extends CustomTitleActivity {
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.live_show_screen);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.default_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.go_home) {
			HomeScreenActivity.start(this);
		}
		return super.onOptionsItemSelected(item);
	}
}
