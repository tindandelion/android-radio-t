package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.live.chat.ProgressListener;
import org.dandelion.radiot.robolectric.RadiotRobolectricRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@RunWith(RadiotRobolectricRunner.class)
public class HttpTranslationStateTest {
    private final ProgressListener listener = mock(ProgressListener.class);
    private HttpTranslationEngine mockEngine = mock(HttpTranslationEngine.class);

    private HttpTranslationState state;

    @Test
    public void disconnectedState_onStart_commandsEngineToConnect() throws Exception {
        state = new HttpTranslationState.Disconnected(mockEngine);
        state.onStart();
        verify(mockEngine).connect();
    }

    @Test
    public void connectingState_whenError_commandsEngineToDisconnect() throws Exception {
        state = new HttpTranslationState.Connecting(mockEngine, listener);
        state.onError();
        verify(mockEngine).disconnect();
    }

    @Test
    public void connectingState_onStart_onlyReportsToListener() throws Exception {
        state = new HttpTranslationState.Connecting(mockEngine, listener);
        state.onStart();

        verify(mockEngine, never()).connect();
        verify(listener).onConnecting();
    }

    @Test
    public void connectingState_onRequestCompleted_notifiesListener() throws Exception {
        state = new HttpTranslationState.Connecting(mockEngine, listener);
        state.onRequestCompleted();
        verify(listener).onConnected();
    }

    @Test
    public void listeningState_onStop_commandsEngineToStopListening() throws Exception {
        state = new HttpTranslationState.Listening(mockEngine);
        state.onStop();
        verify(mockEngine).stopListening();
    }

    @Test
    public void pausedState_onStart_commandsEngineToStartListening() throws Exception {
        state = new HttpTranslationState.Paused(mockEngine);
        state.onStart();
        verify(mockEngine).startListening();
    }
}
