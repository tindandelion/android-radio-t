package org.dandelion.radiot;

import android.app.ListActivity;
import android.content.Intent;
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
		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		listAdapter.add("Подкасты");
		listAdapter.add("После-шоу");
		listAdapter.add("Прямой эфир");
		setListAdapter(listAdapter);
	}
}
