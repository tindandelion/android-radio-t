package org.dandelion.radiot.live.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.dandelion.radiot.R;

public class ChatMessageView extends RelativeLayout {
    public ChatMessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setMessage(String message) {
        TextView messageView = (TextView) findViewById(R.id.message);
        messageView.setText(message);
    }
}
