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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

public class LiveShowTopics extends FrameLayout implements ILiveShowTopicsView,
		AsyncFeedParser.ProgressListener {
	private static String RSS_FEED_URL = "http://www.google.com/reader/public/atom/user%2F04446244743329501593%2Flabel%2FFor%20Radio-T";

	private LiveShowTopicsPresenter presenter;
	private ArrayAdapter<ShowTopic> listAdapter;

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

		presenter = new LiveShowTopicsPresenter(this, createFeedParser());
	}

	private IFeedParser createFeedParser() {
		IFeedParser realParser = AtomFeedParser.withRemoteFeed(RSS_FEED_URL);
		return new AsyncFeedParser(realParser, this);
	}

	private void initListView() {
		listAdapter = new ArrayAdapter<ShowTopic>(getContext(),
				android.R.layout.simple_list_item_1);
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(onItemClicked);
	}

	private void inflateLayout() {
		LayoutInflater inflater = (LayoutInflater) this.getContext()
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
