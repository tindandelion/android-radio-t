package org.dandelion.radiot.live.chat;

import org.dandelion.radiot.live.chat.http.HttpChatClient;
import org.dandelion.radiot.live.chat.http.HttpChatTranslation;
import org.dandelion.radiot.live.chat.http.HttpTranslationState;
import org.dandelion.radiot.live.schedule.DeterministicScheduler;
import org.dandelion.radiot.robolectric.RadiotRobolectricRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@RunWith(RadiotRobolectricRunner.class)
public class HttpChatTranslationTest {
    private static final List<Message> MESSAGE_LIST = Collections.emptyList();
    private final HttpChatClient chatClient = mock(HttpChatClient.class);
    private final DeterministicScheduler pollScheduler = new DeterministicScheduler();
    private final HttpChatTranslation translation = new HttpChatTranslation(chatClient, pollScheduler);
    private final MessageConsumer consumer = mock(MessageConsumer.class);
    private final ProgressListener listener = mock(ProgressListener.class);

    @Test
    public void whenStoppedWhileConnecting_doesNotStartPolling() throws Exception {
        when(chatClient.retrieveMessages("last")).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                translation.stop();
                return null;
            }
        });
        translation.start();
        assertFalse(pollScheduler.isScheduled());
    }

    @Test
    public void whenRestartedWhileConnecting_startsPolling() throws Exception {
        when(chatClient.retrieveMessages("last")).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                translation.stop();
                translation.start();
                return null;
            }
        });
        translation.start();

        assertTrue("restarted", pollScheduler.isScheduled());
    }

    @Test
    public void whenRestartedWhileRefreshing() throws Exception {
        translation.start();

        when(chatClient.retrieveMessages("next")).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                translation.stop();
                translation.start();
                return null;
            }
        });

        assertTrue("restarted", pollScheduler.isScheduled());
    }

    @Test
    public void whenStoppedWhileRefreshing_stopsPolling() throws Exception {
        translation.start();

        when(chatClient.retrieveMessages("next")).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                translation.stop();
                return null;
            }
        });

        pollScheduler.performAction();
        assertFalse(pollScheduler.isScheduled());
    }

    @Test
    public void whenRestarted_startsPolling() throws Exception {
        translation.start();
        assertThat(translation.currentState(), instanceOf(HttpTranslationState.Listening.class));

        translation.stop();
        assertThat(translation.currentState(), instanceOf(HttpTranslationState.Paused.class));
        assertFalse("stopped", pollScheduler.isScheduled());

        translation.start();
        assertThat(translation.currentState(), instanceOf(HttpTranslationState.Listening.class));
        assertTrue("restarted", pollScheduler.isScheduled());
    }

    @Test
    public void whenShutsDown_closesHttpConnection() throws Exception {
        translation.shutdown();
        verify(chatClient).shutdown();
    }

    @Test
    public void whenShutdownWhileStarting_DoNotNotifyListenerOfErrors() throws Exception {
        when(chatClient.retrieveMessages("last")).then(shutdownAndThrowError());

        translation.start();
        verify(listener, never()).onError();
    }

    @Test
    public void whenShutdownWhileStarting_SuppressAllNotifications() throws Exception {
        when(chatClient.retrieveMessages("last")).then(shutdownAndReturnMessages(MESSAGE_LIST));

        translation.start();
        verify(listener, never()).onConnected();
        verify(consumer, never()).processMessages(MESSAGE_LIST);
    }

    @Test
    public void whenShutdownWhileRefreshing_SuppressMessageConsuming() throws Exception {
        when(chatClient.retrieveMessages("next")).then(shutdownAndReturnMessages(MESSAGE_LIST));
        translation.start();
        reset(consumer);

        pollScheduler.performAction();
        verify(consumer, never()).processMessages(MESSAGE_LIST);
    }

    @Before
    public void setUp() throws Exception {
        translation.setProgressListener(listener);
        translation.setMessageConsumer(consumer);
    }

    private Answer<Void> shutdownAndThrowError() {
        return new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                translation.shutdown();
                throw new IOException();
            }
        };
    }

    private Answer<List<Message>> shutdownAndReturnMessages(final List<Message> messages) {
        return new Answer<List<Message>>() {
            @Override
            public List<Message> answer(InvocationOnMock invocation) throws Throwable {
                translation.shutdown();
                return messages;
            }
        };
    }
}
