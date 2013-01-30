package org.dandelion.radiot.live.ui;

import android.app.Activity;
import org.dandelion.radiot.live.chat.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;

@RunWith(RadiotRobolectricRunner.class)
public class ChatTranslationFragmentTest {
    @Test
    public void clearMessageListOnResume() throws Exception {
        ChatStreamAdapter adapter = new ChatStreamAdapter(new Activity(), 10, 10);
        ChatTranslation translation = new HttpChatTranslation(mock(HttpChatClient.class));

        adapter.add(new Message("", "", ""));
        ChatTranslationFragment.doResume(translation, adapter, mock(ChatStreamView.class), null);

        assertThat(adapter.getCount(), equalTo(0));
    }
}
