package org.dandelion.radiot.live.ui;

import android.widget.ArrayAdapter;
import org.dandelion.radiot.live.chat.ProgressListener;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ChatProgressControllerTest {
    private ArrayAdapter adapter = mock(ArrayAdapter.class);
    private ChatTranslationFragment view = mock(ChatTranslationFragment.class);
    private ProgressListener controller = new ChatProgressController(adapter, view);

    @Test
    public void onConnecting_clearsMessageView() throws Exception {
        controller.onConnecting();
        verify(adapter).clear();
    }

    @Test
    public void onConnecting_hidesErrorMessage() throws Exception {
        controller.onConnecting();
        verify(view).hideError();
    }

    @Test
    public void onError_showsErrorMessage() throws Exception {
        controller.onError();
        verify(view).showError();
    }
}
