package org.dandelion.radiot.live.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.dandelion.radiot.R;
import org.dandelion.radiot.live.chat.ChatMessage;

public class ChatMessageView extends RelativeLayout {
    public ChatMessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setMessage(ChatMessage message) {
        setSender(message.sender);
        setBody(message.body);
    }

    private void setSender(String value) {
        TextView messageView = (TextView) findViewById(R.id.sender);
        messageView.setText(value);
    }

    private void setBody(String value) {
        TextView messageView = (TextView) findViewById(R.id.message);
        messageView.setText(value);
    }
}
