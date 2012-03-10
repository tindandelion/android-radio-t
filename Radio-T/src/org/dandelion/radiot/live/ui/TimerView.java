package org.dandelion.radiot.live.ui;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.widget.TextView;

public class TimerView extends TextView {
    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTime(long seconds) {
        setText(DateUtils.formatElapsedTime(seconds));
    }
}
