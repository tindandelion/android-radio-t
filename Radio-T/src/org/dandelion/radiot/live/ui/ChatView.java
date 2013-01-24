package org.dandelion.radiot.live.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ChatView extends ListView {
    public ChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean atBottom() {
        return getLastVisiblePosition() == (getCount() - 1);
    }

    public void scrollToBottom() {
        smoothScrollToPosition(getCount());
    }
}
