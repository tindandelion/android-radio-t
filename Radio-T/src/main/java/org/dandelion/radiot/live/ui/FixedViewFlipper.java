package org.dandelion.radiot.live.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ViewFlipper;

public class FixedViewFlipper extends ViewFlipper {
    public FixedViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDetachedFromWindow() {
        try {
            super.onDetachedFromWindow();
        } catch (IllegalArgumentException e) {
            stopFlipping();
        }
    }
}
