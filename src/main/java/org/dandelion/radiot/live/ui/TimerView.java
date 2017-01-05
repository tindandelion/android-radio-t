package org.dandelion.radiot.live.ui;

import android.content.Context;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class TimerView extends TextView {
    private long startTimestamp;
    private Handler handler;
    private Runnable onTick = new Runnable() {
        @Override
        public void run() {
            tick();
            handler.postDelayed(this, 1000);
        }
    };
    private boolean isActive;

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        handler = new Handler();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility != View.VISIBLE) {
            deactivate();
        }
    }

    public void start(long startTimestamp) {
        if (isActive) {
            deactivate();
        }
        this.startTimestamp = startTimestamp;
        activate();
    }

    public void stop() {
        deactivate();
        setTime(0);
    }

    private void activate() {
        handler.post(onTick);
        isActive = true;
    }

    private void deactivate() {
        handler.removeCallbacks(onTick);
        isActive = false;
    }

    private void tick() {
        long delta = System.currentTimeMillis() - startTimestamp;
        setTime(delta / 1000);
    }

    private void setTime(long seconds) {
        setText(DateUtils.formatElapsedTime(seconds));
    }
}
