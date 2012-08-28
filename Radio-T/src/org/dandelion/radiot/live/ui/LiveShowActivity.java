package org.dandelion.radiot.live.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import org.dandelion.radiot.R;
import org.dandelion.radiot.home_screen.HomeScreenActivity;
import org.dandelion.radiot.live.LiveShowApp;
import org.dandelion.radiot.live.LiveShowClient;
import org.dandelion.radiot.util.CustomTitleActivity;

public class LiveShowActivity extends CustomTitleActivity {
    protected LiveShowClient client;
	private LiveShowPresenter presenter;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.live_show_screen);
        initPresenter();
    }

    private void initPresenter() {
        LiveShowFragment fragment = (LiveShowFragment) getSupportFragmentManager().findFragmentById(R.id.live_show_view);
        presenter = new LiveShowPresenter(fragment);
    }

    @Override
	protected void onStart() {
		super.onStart();
        client = createClient();
        client.addListener(presenter);
	}

    private LiveShowClient createClient() {
        return LiveShowApp.getInstance().createClient(this);
    }

    @Override
	protected void onStop() {
        client.removeListener(presenter);
		super.onStop();
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

	@SuppressWarnings("UnusedParameters")
    public void onButtonPressed(View v) {
        client.togglePlayback();
    }

}
