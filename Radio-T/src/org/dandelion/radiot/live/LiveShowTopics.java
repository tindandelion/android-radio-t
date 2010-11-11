package org.dandelion.radiot.live;

import java.util.List;

import org.dandelion.radiot.R;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class LiveShowTopics extends LinearLayout {
	private LiveShowTopicsPresenter presenter;
	private ArrayAdapter<ShowTopic> listAdapter;
	private OnItemClickListener onTopicClicked = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ShowTopic topic = listAdapter.getItem(position);
			
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(topic.url));
			getContext().startActivity(intent);
		}
	};

	public LiveShowTopics(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflateLayout();
		initListView();

		presenter = new LiveShowTopicsPresenter(this);
	}

	private void initListView() {
		ListView view = (ListView) findViewById(R.id.live_topics_list);
		listAdapter = new ArrayAdapter<ShowTopic>(getContext(),
				android.R.layout.simple_list_item_1);
		view.setAdapter(listAdapter);
		view.setOnItemClickListener(onTopicClicked);
	}

	private void inflateLayout() {
		LayoutInflater inflater = (LayoutInflater) this.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.live_show_topics, this);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		presenter.refreshTopics();
	}

	public void setTopics(List<ShowTopic> topics) {
		listAdapter.clear();
		for (ShowTopic topic : topics) {
			listAdapter.add(topic);
		}
	}
}
