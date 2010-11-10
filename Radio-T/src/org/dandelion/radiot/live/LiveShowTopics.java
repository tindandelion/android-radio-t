package org.dandelion.radiot.live;

import org.dandelion.radiot.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class LiveShowTopics extends LinearLayout {
	public LiveShowTopics(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.live_show_topics, this);
	}
}
