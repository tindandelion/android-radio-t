package org.dandelion.radiot.live.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import org.dandelion.radiot.R;
import org.dandelion.radiot.live.chat.Message;
import org.dandelion.radiot.live.chat.MessageConsumer;

import java.util.List;

class ChatStreamAdapter extends ArrayAdapter<Message>
        implements MessageConsumer {

    private final LayoutInflater inflater;
    private final int messageLimit;

    public ChatStreamAdapter(Context context, int messageLimit) {
        super(context, 0);
        this.messageLimit = messageLimit;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessageView row = (ChatMessageView) convertView;
        if (row == null) {
            row = (ChatMessageView) inflater.inflate(R.layout.chat_message, parent, false);
        }
        row.setMessage(getItem(position));
        return row;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public void appendMessages(List<Message> messages) {
        for (Message msg : messages) {
            add(msg);
        }
        shrinkList();
    }

    private void shrinkList() {
        if (exceedsMessageLimit()) {
            shrinkToNormalSize();
        }
    }

    private void shrinkToNormalSize() {
        for (int shrinkCount = getCount()-messageLimit; shrinkCount > 0; shrinkCount--) {
            remove(getItem(0));
        }
    }

    private boolean exceedsMessageLimit() {
        return getCount() > (messageLimit * 2);
    }
}
