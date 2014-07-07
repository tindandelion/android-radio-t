package org.dandelion.radiot.live.ui;

import android.widget.ArrayAdapter;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ChatProgressControllerTest {

    private ArrayAdapter adapter = mock(ArrayAdapter.class);
    private ChatTranslationFragment view = mock(ChatTranslationFragment.class);
    private ChatProgressController controller = new ChatProgressController(view, adapter);

    @Test
    public void onConnecting_clearsArrayAdapter() throws Exception {
        controller.onConnecting();
        verify(adapter).clear();
    }

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
        controller.accept(new Exception());
        verify(view).showError();
    }

    @Test
    public void onError_hidesProgressIndicator() throws Exception {
        controller.accept(new Exception());
        verify(view).hideProgress();
    }
}
