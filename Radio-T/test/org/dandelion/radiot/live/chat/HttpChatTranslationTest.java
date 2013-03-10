package org.dandelion.radiot.live.chat;

import org.dandelion.radiot.live.chat.http.HttpChatClient;
import org.dandelion.radiot.live.chat.http.HttpChatTranslation;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@RunWith(RadiotRobolectricRunner.class)
public class HttpChatTranslationTest {
    private static final List<Message> MESSAGE_LIST = Collections.emptyList();
    private final HttpChatClient chatClient = mock(HttpChatClient.class);
    private final DeterministicScheduler refreshScheduler = new DeterministicScheduler();
    private final HttpChatTranslation translation = new HttpChatTranslation(chatClient, refreshScheduler);
    private final MessageConsumer consumer = mock(MessageConsumer.class);
    private final ProgressListener listener = mock(ProgressListener.class);

    @Test
    // TODO: Moved to HttpTranslationStateTest
    public void whenStartingFirstTime_requestsLastMessages() throws Exception {
        when(chatClient.retrieveMessages("last")).thenReturn(MESSAGE_LIST);
        translation.start();
        verify(consumer).processMessages(MESSAGE_LIST);
    }

    @Test
    // TODO: Moved to HttpTranslationStateTest
    public void whenStarting_notifiesListenerOfProgress() throws Exception {
        when(chatClient.retrieveMessages("last")).thenReturn(MESSAGE_LIST);
        translation.start();

        verify(listener).onConnecting();
        verify(listener).onConnected();
    }

    @Test
    // TODO: Moved to HttpTranslationStateTest
    public void whenStatring_butErrorOccurs_notifiesListener() throws Exception {
        when(chatClient.retrieveMessages("last")).thenThrow(IOException.class);
        translation.start();

        verify(listener).onError();
        verifyZeroInteractions(consumer);
    }

    @Test
    // TODO: Moved to HttpTranslationStateTest
    public void whenStarted_schedulesRefresh() throws Exception {
        when(chatClient.retrieveMessages("next")).thenReturn(MESSAGE_LIST);
        translation.start();

        reset(consumer);
        refreshScheduler.performAction();
        verify(consumer).processMessages(MESSAGE_LIST);
    }

    @Test
    // TODO: Moved to HttpTranslationStateTest
    public void whenRefreshing_schedulesNextRefresh() throws Exception {
        when(chatClient.retrieveMessages("next")).thenReturn(MESSAGE_LIST);

        translation.start();

        reset(consumer);
        refreshScheduler.performAction();
        verify(consumer).processMessages(MESSAGE_LIST);
        assertTrue(refreshScheduler.isScheduled());
    }

    @Test
    public void whenRefreshing_butErrorOccurs_notifiesListener() throws Exception {
        translation.start();

        reset(consumer);
        when(chatClient.retrieveMessages("next")).thenThrow(IOException.class);
        refreshScheduler.performAction();

        verify(listener).onError();
        verifyZeroInteractions(consumer);
    }

    @Test
    public void whenRestarting_doNotRequestLastMessages() throws Exception {
        when(chatClient.retrieveMessages("last")).thenReturn(MESSAGE_LIST);

        translation.start();
        translation.stop();
        translation.start();

        verify(chatClient, times(1)).retrieveMessages("last");
    }

    @Test
    public void whenRestarting_reschedulesRefresh() throws Exception {
        when(chatClient.retrieveMessages("last")).thenReturn(MESSAGE_LIST);

        translation.start();
        translation.stop();
        translation.start();

        assertTrue(refreshScheduler.isScheduled());
    }

    @Test
    public void whenRestarting_whileConnecting_doesNotScheduleRefreshTwice() throws Exception {
        when(chatClient.retrieveMessages("last"))
                .then(restartAndReturnMessages(MESSAGE_LIST));

        translation.start();
        assertEquals(1, refreshScheduler.scheduleCount());
    }

    @Test
    public void whenRestarting_whileConnecting_callsListenerTwice() throws Exception {
        when(chatClient.retrieveMessages("last"))
                .then(restartAndReturnMessages(MESSAGE_LIST));

        translation.start();
        verify(listener, times(2)).onConnecting();
        verify(listener, times(1)).onConnected();
    }

    @Test
    public void whenStopping_cancelsScheduledRefresh() throws Exception {
        translation.start();
        translation.stop();

        assertFalse(refreshScheduler.isScheduled());
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
    public void whenStoppedWhileRefreshing_SuppressMessageConsuming() throws Exception {
        when(chatClient.retrieveMessages("next")).then(shutdownAndReturnMessages(MESSAGE_LIST));
        translation.start();
        reset(consumer);

        refreshScheduler.performAction();
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

    private Answer<?> restartAndReturnMessages(final List<Message> messages) {
        return new Answer<List<Message>>() {
            @Override
            public List<Message> answer(InvocationOnMock invocation) throws Throwable {
                translation.start();
                return messages;
            }
        };
    }
}
