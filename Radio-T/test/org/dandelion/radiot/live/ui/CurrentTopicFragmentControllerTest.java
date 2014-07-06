package org.dandelion.radiot.live.ui;

import android.os.Bundle;
import org.dandelion.radiot.live.topics.CurrentTopic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class CurrentTopicFragmentControllerTest {
    private CurrentTopicFragment view = mock(CurrentTopicFragment.class);
    private CurrentTopicFragment.Controller controller = new CurrentTopicFragment.Controller(view);

    @Test
    public void onCreate_viewIsHiddenInitially() throws Exception {
        controller.onViewCreated(null);
        verify(view).hideImmediately();
    }

    @Test
    public void onCreate_viewIsShownWithText_ifShownBefore() throws Exception {
        when(view.isHidden()).thenReturn(false);
        when(view.getTopicText()).thenReturn("Current topic");

        recreateController();

        verify(view).setTopicText("Current topic");
        verify(view).showImmediately();
    }

    @Test
    public void onCreate_viewIsHidden_ifHiddenBefore() throws Exception {
        when(view.isHidden()).thenReturn(true);
        when(view.getTopicText()).thenReturn("Current topic");

        recreateController();

        verify(view).hideImmediately();
    }

    @Test
    public void whenNewTopicAccepted_updatesView_andShowsIt() throws Exception {
        CurrentTopic topic = new CurrentTopic("id", "New topic");
        controller.accept(topic);

        verify(view).setTopicText(topic.text);
        verify(view).showAnimated();
    }

    @Test
    public void whenSameTopicAccepted_preservesCurrentViewState() throws Exception {
        CurrentTopic topic = new CurrentTopic("id", "New topic");
        controller.accept(topic);

        reset(view);
        controller.accept(topic);
        verifyZeroInteractions(view);
    }

    @Test
    public void whenSameTopicAcceptedAfterRecreation_preservesCurrentViewState() throws Exception {
        CurrentTopic topic = new CurrentTopic("id", "New topic");
        controller.accept(topic);

        recreateController();
        reset(view);

        controller.accept(topic);
        verifyZeroInteractions(view);
    }

    private void recreateController() {
        Bundle savedState = new Bundle();
        controller.saveState(savedState);

        controller = new CurrentTopicFragment.Controller(view);
        controller.onViewCreated(savedState);
    }
}