package org.dandelion.radiot;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HomeScreen extends ListActivity {
	private static final String PODCAST_URL = "http://feeds.rucast.net/radio-t";
	private static final String PIRATES_URL = "http://feeds.feedburner.com/pirate-radio-t";
	private ArrayAdapter<HomeScreenItem> listAdapter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);
		initList();
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		HomeScreenItem item = listAdapter.getItem(position);
		item.execute();
	}

	private void initList() {
		listAdapter = new ArrayAdapter<HomeScreenItem>(this,
				R.layout.home_screen_item, R.id.home_screen_item_title, getHomeSceenItems());
		setListAdapter(listAdapter);
	}

	private List<HomeScreenItem> getHomeSceenItems() {
		List<HomeScreenItem> items = new ArrayList<HomeScreenItem>();
		items.add(new HomeScreenItem(R.string.main_show_title) {
			@Override
			public void execute() {
				PodcastListActivity.start(HomeScreen.this, this.toString(), PODCAST_URL);
			}
		});
		items.add(new HomeScreenItem(R.string.after_show_title) {
			@Override
			public void execute() {
				PodcastListActivity.start(HomeScreen.this, this.toString(), PIRATES_URL);
			}
		});
		items.add(new HomeScreenItem(R.string.on_air_title) {
			@Override
			public void execute() {
				startActivity(new Intent(HomeScreen.this, OnAirActivity.class));
			}
		});
		return items;
	}
	
	class HomeScreenItem {
		private String title;

		public HomeScreenItem(int titleId) {
			this.title = getString(titleId);
		}
		
		public void execute() {
		}

		@Override
		public String toString() {
			return title;
		}
	}
}
