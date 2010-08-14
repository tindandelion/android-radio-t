package org.dandelion.radiot;

import java.util.ArrayList;
import java.util.List;

import org.dandelion.radiot.live.LiveShowActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class HomeScreen extends RadiotActivity implements OnItemClickListener {
	private final HomeScreenItem separatorItem = new HomeScreenItem(0, 0);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);
		initList(R.id.podcasts_menu, getPodcastMenuItems());
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
						"main-show");
			}
		});
		items.add(new HomeScreenItem(R.string.after_show_title,
				R.drawable.after_show_icon) {
			@Override
			public void execute() {
				PodcastListActivity.start(HomeScreen.this, this.title,
						"after-show");
			}
		});
		items.add(new HomeScreenItem(R.string.live_show_title,
				R.drawable.live_show_icon) {
			@Override
			public void execute() {
				startActivity(new Intent(HomeScreen.this,
						LiveShowActivity.class));
			}
		});
		items.add(separatorItem);
		items.add(new HomeScreenItem(R.string.about_app_title,
				R.drawable.about_icon) {
			@Override
			public void execute() {
				startActivity(new Intent(HomeScreen.this, AboutAppScreen.class));
			}
		});
		return items;
	}

	class HomeScreenItem {
		public String title;
		public int iconId = 0;

		public HomeScreenItem(int titleId, int iconId) {
			if (titleId > 0) {
				this.title = getString(titleId);
			}
			this.iconId = iconId;
		}

		public void execute() {
		}

		public boolean hasIcon() {
			return iconId > 0;
		}

		public boolean isSeparator() {
			return title == null;
		}
	}

	class HomeScreenAdapter extends ArrayAdapter<HomeScreenItem> {

		private static final float SEPARATOR_HEIGHT_DIP = 32;

		public HomeScreenAdapter(List<HomeScreenItem> items) {
			super(HomeScreen.this, R.layout.home_screen_item, items);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HomeScreenItem item = getItem(position);
			if (item.isSeparator()) {
				return buildSeparatorRow();
			} else {
				View row = getLayoutInflater().inflate(
						R.layout.home_screen_item, null, false);
				return buildViewForItem(row, item);
			}
		}

		private View buildSeparatorRow() {
			View view = new View(HomeScreen.this);
			view.setMinimumHeight(getSeparatorHeight());
			return view;
		}

		private int getSeparatorHeight() {
			float scale = getContext().getResources().getDisplayMetrics().density;
			return (int) (scale * SEPARATOR_HEIGHT_DIP + 0.5f);
		}

		private View buildViewForItem(View row, HomeScreenItem item) {
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

		@Override
		public boolean isEnabled(int position) {
			return !this.getItem(position).isSeparator();
		}

	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		HomeScreenItem item = (HomeScreenItem) parent.getAdapter().getItem(
				position);
		item.execute();
	}
}
