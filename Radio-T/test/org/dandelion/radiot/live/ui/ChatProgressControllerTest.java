package org.dandelion.radiot.live.ui;

import org.dandelion.radiot.live.chat.ProgressListener;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ChatProgressControllerTest {
    private ChatTranslationFragment view = mock(ChatTranslationFragment.class);
    private ProgressListener controller = new ChatProgressController(view);

    @Test
    public void onConnecting_hidesErrorMessage() throws Exception {
        controller.onConnecting();
        verify(view).hideError();
    }

    @Test
    public void onConnecting_showsProgressIndicator() throws Exception {
        controller.onConnecting();
        verify(view).showProgress();
    }

    @Test
    public void onConnected_hidesProgressIndicator() throws Exception {
        controller.onConnected();
        verify(view).hideProgress();
    }

    @Test
    public void onError_showsErrorMessage() throws Exception {
        controller.onError();
        verify(view).showError();
    }

    @Test
    public void onError_hidesProgressIndicator() throws Exception {
        controller.onError();
        verify(view).hideProgress();
    }
}
