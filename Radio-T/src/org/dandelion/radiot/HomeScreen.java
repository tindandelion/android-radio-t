package org.dandelion.radiot;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class HomeScreen extends Activity implements OnItemClickListener {
	private static final String PODCAST_URL = "http://feeds.rucast.net/radio-t";
	private static final String PIRATES_URL = "http://feeds.feedburner.com/pirate-radio-t";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);
		initList(R.id.podcasts_menu, getPodcastMenuItems());
		initList(R.id.info_menu, getInfoMenuItems());
	}

	private List<HomeScreenItem> getInfoMenuItems() {
		ArrayList<HomeScreenItem> items = new ArrayList<HomeScreenItem>();
		items.add(new HomeScreenItem(R.string.about_app_title, 0) {
			@Override
			public void execute() {
				startActivity(new Intent(HomeScreen.this,
						AboutAppScreen.class));
			}
		});
		return items;
	}

	private void initList(int listId, List<HomeScreenItem> items) {
		HomeScreenAdapter adapter = new HomeScreenAdapter(items);
		ListView listView = (ListView) findViewById(listId);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	private List<HomeScreenItem> getPodcastMenuItems() {
		List<HomeScreenItem> items = new ArrayList<HomeScreenItem>();
		items.add(new HomeScreenItem(R.string.main_show_title,
				R.drawable.podcast_icon) {
			@Override
			public void execute() {
				PodcastListActivity.start(HomeScreen.this, this.title,
						PODCAST_URL);
			}
		});
		items.add(new HomeScreenItem(R.string.after_show_title,
				R.drawable.after_show_icon) {
			@Override
			public void execute() {
				PodcastListActivity.start(HomeScreen.this, this.title,
						PIRATES_URL);
			}
		});
		items.add(new HomeScreenItem(R.string.live_show_title,
				R.drawable.live_show_icon) {
			@Override
			public void execute() {
				startActivity(new Intent(HomeScreen.this,
						LiveShowScreen.class));
			}
		});
		return items;
	}

	class HomeScreenItem {
		public String title;
		public int iconId = 0;

		public HomeScreenItem(int titleId, int iconId) {
			this.title = getString(titleId);
			this.iconId = iconId;
		}

		public void execute() {
		}

		public boolean hasIcon() {
			return iconId > 0;
		}
	}

	class HomeScreenAdapter extends ArrayAdapter<HomeScreenItem> {

		public HomeScreenAdapter(List<HomeScreenItem> items) {
			super(HomeScreen.this, R.layout.home_screen_item, items);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = getLayoutInflater();
				row = inflater
						.inflate(R.layout.home_screen_item, parent, false);
			}

			HomeScreenItem item = getItem(position);

			TextView text = (TextView) row
					.findViewById(R.id.home_screen_item_title);
			text.setText(item.title);

			ImageView icon = (ImageView) row
					.findViewById(R.id.home_screen_item_icon);
			if (item.hasIcon()) {
				icon.setImageResource(item.iconId);
			}

			return row;
		}

	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		HomeScreenItem item = (HomeScreenItem) parent.getAdapter().getItem(
				position);
		item.execute();
	}
}
