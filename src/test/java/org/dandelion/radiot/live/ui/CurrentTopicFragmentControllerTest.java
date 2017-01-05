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
        setViewIsShownWithText("Current topic");

        recreateController();

        verify(view).setTopicText("Current topic");
        verify(view).showImmediately();
    }

    @Test
    public void onCreate_viewIsHidden_ifHiddenBefore() throws Exception {
        setViewIsHidden();

        recreateController();

        verify(view).hideImmediately();
    }

    @Test
    public void whenNewTopicAccepted_updatesView_andShowsIt() throws Exception {
        CurrentTopic topic = CurrentTopic.create("id", "New topic");
        controller.accept(topic);

        verify(view).setTopicText(topic.text);
        verify(view).showAnimated();
    }

    @Test
    public void whenSameTopicAccepted_preservesCurrentViewState() throws Exception {
        CurrentTopic topic = CurrentTopic.create("id", "New topic");
        controller.accept(topic);

        reset(view);
        controller.accept(topic);
        verifyZeroInteractions(view);
    }

    @Test
    public void whenSameTopicAcceptedAfterRecreation_preservesCurrentViewState() throws Exception {
        CurrentTopic topic = CurrentTopic.create("id", "New topic");
        controller.accept(topic);

        recreateController();
        reset(view);

        controller.accept(topic);
        verifyZeroInteractions(view);
    }

    @Test
    public void whenTopicGoesEmpty_hidesView() throws Exception {
        setViewIsShownWithText("Current topic");

        controller.accept(CurrentTopic.empty());
        verify(view).hideAnimated();
    }

    private void setViewIsShownWithText(String text) {
        when(view.isHidden()).thenReturn(false);
        when(view.getTopicText()).thenReturn(text);
    }

    private void setViewIsHidden() {
        when(view.isHidden()).thenReturn(true);
    }

    private void recreateController() {
        Bundle savedState = new Bundle();
        controller.saveState(savedState);

        controller = new CurrentTopicFragment.Controller(view);
        controller.onViewCreated(savedState);
    }
}