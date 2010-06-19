package org.dandelion.radiot;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

public class PodcastListActivity extends ListActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    setPodcastList(new String[]{"#121", "#122", "#123"});
	}

	public void setPodcastList(String[] items) {
		ListAdapter listAdaptor = new ArrayAdapter<String>(this, 
		android.R.layout.simple_list_item_1, items);
		
		setListAdapter(listAdaptor);
	}

}
