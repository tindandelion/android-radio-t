package org.dandelion.radiot.live.chat;

import org.dandelion.radiot.live.schedule.DeterministicScheduler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
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
    public void onStart_RequestsLastMessages() throws Exception {
        when(chatClient.retrieveMessages("last")).thenReturn(MESSAGE_LIST);
        translation.start(consumer);

        verify(consumer).appendMessages(MESSAGE_LIST);
    }

    @Test
    public void onStart_SchedulesRefresh() throws Exception {
        when(chatClient.retrieveMessages("next")).thenReturn(MESSAGE_LIST);
        translation.start(consumer);

        reset(consumer);
        refreshScheduler.performAction();
        verify(consumer).appendMessages(MESSAGE_LIST);
    }

    @Test
    public void onStart_NotifiesListener() throws Exception {
        when(chatClient.retrieveMessages("last")).thenReturn(MESSAGE_LIST);
        translation.start(consumer);

        verify(listener).onConnecting();
        verify(listener).onConnected();
    }

    @Test
    public void onStart_whenErrorOccurs_notifiesListener() throws Exception {
        when(chatClient.retrieveMessages("last")).thenThrow(IOException.class);
        translation.start(consumer);

        verify(listener).onError();
        verifyZeroInteractions(consumer);
    }

    @Test
    public void onRefresh_SchedulesNextRefresh() throws Exception {
        when(chatClient.retrieveMessages("next")).thenReturn(MESSAGE_LIST);

        translation.start(consumer);

        reset(consumer);
        refreshScheduler.performAction();
        verify(consumer).appendMessages(MESSAGE_LIST);

        reset(consumer);
        refreshScheduler.performAction();
        verify(consumer).appendMessages(MESSAGE_LIST);
    }

    @Test
    public void onRefresh_whenErrorOccurs_NotifiesListener() throws Exception {
        translation.start(consumer);

        reset(consumer);
        when(chatClient.retrieveMessages("next")).thenThrow(IOException.class);
        refreshScheduler.performAction();

        verify(listener).onError();
        verifyZeroInteractions(consumer);
    }

    @Test
    public void onStop_ShutsDownHttpConnection() throws Exception {
        translation.stop();
        verify(chatClient).shutdown();
    }

    @Test
    public void onStop_CancelsScheduledRefresh() throws Exception {
        translation.start(consumer);
        translation.stop();

        assertFalse(refreshScheduler.isScheduled());
    }

    @Test
    public void whenStoppedWhileStarting_DoNotNotifyListenerOfErrors() throws Exception {
        when(chatClient.retrieveMessages("last")).then(stopAndThrowException());

        translation.start(consumer);
        verify(listener, never()).onError();
    }

    @Test
    public void whenStoppedWhileStarting_SuppressAllNotifications() throws Exception {
        when(chatClient.retrieveMessages("last")).then(stopAndReturnMessages(MESSAGE_LIST));

        translation.start(consumer);
        verify(listener, never()).onConnected();
        verify(consumer, never()).appendMessages(MESSAGE_LIST);
    }

    @Test
    public void whenStoppedWhileRefreshing_SuppressMessageConsuming() throws Exception {
        when(chatClient.retrieveMessages("next")).then(stopAndReturnMessages(MESSAGE_LIST));
        translation.start(consumer);
        reset(consumer);

        refreshScheduler.performAction();
        verify(consumer, never()).appendMessages(MESSAGE_LIST);
    }

    @Before
    public void setUp() throws Exception {
        translation.setProgressListener(listener);
    }

    private Answer<Void> stopAndThrowException() {
        return new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                translation.stop();
                throw new IOException();
            }
        };
    }

    private Answer<List<Message>> stopAndReturnMessages(final List<Message> messages) {
        return new Answer<List<Message>>() {
            @Override
            public List<Message> answer(InvocationOnMock invocation) throws Throwable {
                translation.stop();
                return messages;
            }
        };
    }
}
