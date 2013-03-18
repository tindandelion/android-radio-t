package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.live.chat.Message;
import org.dandelion.radiot.live.chat.MessageConsumer;
import org.dandelion.radiot.live.chat.ProgressListener;
import org.dandelion.radiot.live.schedule.Scheduler;
import org.dandelion.radiot.robolectric.RadiotRobolectricRunner;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
@RunWith(RadiotRobolectricRunner.class)
public class HttpTranslationEngineTest {
    private final Scheduler scheduler  = mock(Scheduler.class);
    private final HttpChatClient chatClient = mock(HttpChatClient.class);
    private final MessageConsumer consumer = mock(MessageConsumer.class);
    private final ProgressListener listener = mock(ProgressListener.class);
    private final HttpTranslationEngine engine =
            new HttpTranslationEngine(chatClient, consumer, listener, scheduler);

    @Test
    public void cancelPolling() throws Exception {
        engine.cancelPoll();
        verify(scheduler).cancel();
    }

    @Test
    public void disconnect() throws Exception {
        engine.disconnect();
        assertThat(engine, isInState(HttpTranslationState.Disconnected.class));
    }

    @Test
    public void stopListening() throws Exception {
        engine.stopListening();
        assertThat(engine, isInState(HttpTranslationState.Paused.class));
    }

    @Test
    public void whenConnecting_switchesToConnectingState_whileRetrievingMessages() throws Exception {
        final ArrayList<Message> messages = new ArrayList<Message>();

        when(chatClient.retrieveMessages("last")).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                assertThat(engine, isInState(HttpTranslationState.Connecting.class));
                return messages;
            }
        });
        engine.connectToChat();

        verify(consumer).processMessages(messages);
        assertThat(engine, isInState(HttpTranslationState.Listening.class));
    }

    @Test
    public void whenConnecting_reportsProgress() throws Exception {
        when(chatClient.retrieveMessages("last")).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                verify(listener).onConnecting();
                return null;
            }
        });
        engine.connectToChat();

        verify(listener).onConnected();
    }

    @Test
    public void whenConnecting_reportsError_andGoesDisconnected() throws Exception {
        when(chatClient.retrieveMessages("last")).thenThrow(IOException.class);

        engine.connectToChat();

        verify(listener).onError();
        assertThat(engine, isInState(HttpTranslationState.Disconnected.class));
    }

    @Test
    public void whenConnecting_butInterrupted_switchesToPaused() throws Exception {
        when(chatClient.retrieveMessages("last")).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                engine.currentState().onStop();
                return null;
            }
        });
        engine.connectToChat();

        assertThat(engine, isInState(HttpTranslationState.Paused.class));
    }

    @Test
    public void whenListening_schedulesPolling() throws Exception {
        engine.startListening();

        assertThat(engine, isInState(HttpTranslationState.Listening.class));
        verify(scheduler).setPerformer((HttpTranslationState.Listening)engine.currentState());
        verify(scheduler).scheduleNext();
    }

    private Matcher<? super HttpTranslationEngine> isInState(final Class<? extends HttpTranslationState> aClass) {
        return new TypeSafeMatcher<HttpTranslationEngine>() {
            @Override
            protected boolean matchesSafely(HttpTranslationEngine httpTranslationEngine) {
                return instanceOf(aClass).matches(httpTranslationEngine.currentState());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Engine in state").appendValue(aClass);
            }

            @Override
            protected void describeMismatchSafely(HttpTranslationEngine item, Description mismatchDescription) {
                mismatchDescription.appendText("Engine in state").appendValue(item.currentState().getClass());
            }
        };
    }
}
