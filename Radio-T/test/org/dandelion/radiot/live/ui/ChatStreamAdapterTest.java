package org.dandelion.radiot.live.ui;

import android.app.Activity;
import org.dandelion.radiot.live.chat.Message;
import org.dandelion.radiot.live.chat.RadiotRobolectricRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(RadiotRobolectricRunner.class)
public class ChatStreamAdapterTest {
    @Test
    public void whenListReachesTwiceLimitSize_shrinksListToLimit() throws Exception {
        final int messageLimit = 5;
        ChatStreamAdapter adapter = new ChatStreamAdapter(new Activity(), messageLimit);

        adapter.appendMessages(messages(5));
        assertThat(adapter.getCount(), equalTo(5));

        adapter.appendMessages(messages(3));
        assertThat(adapter.getCount(), equalTo(8));

        adapter.appendMessages(messages(3));
        assertThat(adapter.getCount(), equalTo(messageLimit));
    }



    private ArrayList<Message> messages(int count) {
        ArrayList<Message> result = new ArrayList<Message>(count);
        for (int i = 0; i < count; i++) {
            String str = Integer.toString(i);
            result.add(new Message(str, "", ""));
        }
        return result;
    }
}
