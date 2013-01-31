package org.dandelion.radiot.live.ui;

import android.widget.ArrayAdapter;
import org.dandelion.radiot.live.chat.ErrorListener;
import org.dandelion.radiot.live.chat.RadiotRobolectricRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RadiotRobolectricRunner.class)
public class ChatProgressControllerTest {
    private ArrayAdapter adapter = mock(ArrayAdapter.class);
    private ChatTranslationFragment view = mock(ChatTranslationFragment.class);
    private ErrorListener controller = new ChatProgressController(adapter, view);

    @Test
    public void onConnecting_clearsMessageView() throws Exception {
        controller.onStarting();
        verify(adapter).clear();
    }

    @Test
    public void showsErrorOnError() throws Exception {
        controller.onError();
        verify(view).onError();
    }
}
