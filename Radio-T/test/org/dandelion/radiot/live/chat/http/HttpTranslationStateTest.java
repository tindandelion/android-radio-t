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
    public void disconnectedState_onStart_commandsEngineToStartConnecting() throws Exception {
        state = new HttpTranslationState.Disconnected(mockEngine);
        state.onStart();
        verify(mockEngine).startConnecting();
    }

    @Test
    public void connectingState_whenError_commandsEngineToDisconnect() throws Exception {
        state = new HttpTranslationState.Connecting(mockEngine, listener);
        state.onError();
        verify(mockEngine).disconnect();
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
    public void pausedListeningState_onStart_commandsEngineToResumeListening() throws Exception {
        state = new HttpTranslationState.PausedListening(mockEngine);
        state.onStart();
        verify(mockEngine).resumeListening();
    }

    @Test
    public void pausedConnectingState_onStart_commandsEngineToResumeConnecting() throws Exception {
        state = new HttpTranslationState.PausedConnecting(mockEngine, listener);
        state.onStart();

        verify(mockEngine).resumeConnecting();
    }
}
