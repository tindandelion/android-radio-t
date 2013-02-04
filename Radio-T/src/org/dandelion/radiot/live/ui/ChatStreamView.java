package org.dandelion.radiot.live.ui;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ListView;

public class ChatStreamView extends ListView {
    private static boolean useSmoothScroll = true;
    static {
        useSmoothScroll = Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public ChatStreamView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean atBottom() {
        return getLastVisiblePosition() == (getCount() - 1);
    }

    public void scrollToBottom() {
        int lastPosition = getCount() - 1;
        if (useSmoothScroll) {
            smoothScrollToPosition(lastPosition);
        } else {
            setSelection(lastPosition);
        }
    }
}
