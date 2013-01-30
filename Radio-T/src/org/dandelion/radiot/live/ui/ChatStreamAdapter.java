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
    private final ListSizeGuard limitGuard;

    public ChatStreamAdapter(Context context, int messageLimit, int shrinkSize) {
        super(context, 0);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        limitGuard = new ListSizeGuard(this, messageLimit, shrinkSize);
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
    public void initWithMessages(List<Message> messages) {
        appendMessages(messages);
    }

    @Override
    public void appendMessages(List<Message> messages) {
        for (Message msg : messages) {
            add(msg);
        }
        limitGuard.shrink();
    }

    private static class ListSizeGuard {
        private final ArrayAdapter adapter;
        private final int limit;
        private final int normalSize;

        public ListSizeGuard(ArrayAdapter adapter, int limit, int normalSize) {
            this.adapter = adapter;
            this.limit = limit;
            this.normalSize = normalSize;
        }

        public void shrink() {
            if (exceedsLimit()) {
                shrinkToNormalSize();
            }
        }

        private boolean exceedsLimit() {
            return adapter.getCount() > limit;
        }

        @SuppressWarnings("unchecked")
        private void shrinkToNormalSize() {
            for (int shrinkCount = adapter.getCount() - normalSize; shrinkCount > 0; shrinkCount--) {
                adapter.remove(adapter.getItem(0));
            }
        }
    }
}
