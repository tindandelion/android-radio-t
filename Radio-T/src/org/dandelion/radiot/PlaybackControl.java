package org.dandelion.radiot;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class PlaybackControl extends RelativeLayout {

	public PlaybackControl(Context context, AttributeSet attrs) {
		super(context, attrs);
		((Activity) context).getLayoutInflater().inflate(
				R.layout.playback_control, this, true);
	}

}
