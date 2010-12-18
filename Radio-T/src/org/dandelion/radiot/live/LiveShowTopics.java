package org.dandelion.radiot.live;

import org.dandelion.radiot.R;
import org.dandelion.radiot.rss.AsyncFeedParser;
import org.dandelion.radiot.rss.AtomFeedParser;
import org.dandelion.radiot.rss.IFeedParser;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LiveShowTopics extends FrameLayout implements ILiveShowTopicsView,
		AsyncFeedParser.ProgressListener {
	private static String RSS_FEED_URL = "http://www.google.com/reader/public/atom/user%2F04446244743329501593%2Flabel%2FFor%20Radio-T";

	private LiveTopicsPresenter presenter;
	private LiveTopicsAdapter listAdapter;

	private OnItemClickListener onItemClicked = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			ShowTopic item = listAdapter.getItem(position);
			intent.setData(item.getUri());
			getContext().startActivity(intent);
		}
	};

	private ListView listView;
	private View progressView;

	public LiveShowTopics(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflateLayout();
		initListView();

		presenter = new LiveTopicsPresenter(this, createFeedParser());
	}

	private IFeedParser createFeedParser() {
		IFeedParser realParser = AtomFeedParser.withRemoteFeed(RSS_FEED_URL);
		return new AsyncFeedParser(realParser, this);
	}

	private void initListView() {
		listAdapter = new LiveTopicsAdapter(getContext());
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(onItemClicked);
	}

	private void inflateLayout() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.live_show_topics, this);
		listView = (ListView) findViewById(R.id.live_topics_list);
		progressView = findViewById(R.id.live_topics_progress);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		presenter.refreshTopics();
	}

	public void clearTopics() {
		listAdapter.clear();
	}

	public void addTopic(ShowTopic topic) {
		listAdapter.add(topic);
	}

	public void onStartedReading() {
		progressView.setVisibility(VISIBLE);
	}

	public void onFinishedReading(Exception error) {
		progressView.setVisibility(GONE);
		if (null != error) {
			Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT)
					.show();
		}
	}
}

class LiveTopicsAdapter extends ArrayAdapter<ShowTopic> {
	private LayoutInflater inflater;

	public LiveTopicsAdapter(Context context) {
		super(context, android.R.layout.simple_list_item_1);
		inflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (null == convertView) {
			view = inflater.inflate(R.layout.live_topic_list_item, parent,
					false);
		}
		ShowTopic item = getItem(position);

		TextView titleView = (TextView) view
				.findViewById(R.id.live_topic_item_content);
		titleView.setText(item.title);

		TextView dateView = (TextView) view
				.findViewById(R.id.live_topic_item_datetime);
		dateView.setText("18.12.2010");

		TextView sourceView = (TextView) view
				.findViewById(R.id.live_topic_item_source);
		sourceView.setText("www.google.com");

		return view;
	}
}
