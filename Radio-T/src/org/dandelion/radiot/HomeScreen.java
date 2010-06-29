package org.dandelion.radiot;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HomeScreen extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initList();
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this, PodcastListActivity.class);
		startActivity(intent);
	}

	private void initList() {
		String[] menu_items = getResources().getStringArray(R.array.main_menu);
		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, menu_items);
		setListAdapter(listAdapter);
	}
}
