package org.dandelion.radiot.live.ui;

import android.app.Activity;
import org.dandelion.radiot.live.chat.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ChatStreamAdapterTest {
    @Test
    public void whenListReachesTwiceLimitSize_shrinksListToLimit() throws Exception {
        final int messageLimit = 5;
        final ArrayList<Message> messages = new ArrayList<Message>();
        Activity activity = Robolectric.buildActivity(Activity.class).create().get();
        ChatStreamAdapter adapter = new ChatStreamAdapter(activity, messages, messageLimit);

        adapter.processMessages(messages(5));
        assertThat(messages.size(), equalTo(5));

        adapter.processMessages(messages(3));
        assertThat(messages.size(), equalTo(8));

        adapter.processMessages(messages(3));
        assertThat(messages.size(), equalTo(messageLimit));
    }



    private ArrayList<Message> messages(int count) {
        ArrayList<Message> result = new ArrayList<Message>(count);
        for (int i = 0; i < count; i++) {
            String str = Integer.toString(i);
            result.add(new Message(str, "", "", 0));
        }
        return result;
    }
}
