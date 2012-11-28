package org.dandelion.radiot.endtoend.podcasts.helpers;

import android.app.Activity;
import com.jayway.android.robotium.solo.Solo;
import org.dandelion.radiot.R;
import android.app.Instrumentation;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import org.hamcrest.*;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PodcastListRunner {
    private Solo solo;
    private Activity activity;

    public PodcastListRunner(Instrumentation instrumentation, Activity activity) {
        this.solo = new Solo(instrumentation, activity);
        this.activity = activity;
    }

    public void showsPodcastItem(String number, String date, String description) {
        podcastList(has(anItemWith(number, date, description)));
    }

    public void pressBack() {
        solo.goBack();
    }

    public void wasClosed() {
        assertThat(activity, isFinishing());
    }


    public void showsEmptyList() {
        podcastList(isEmpty());
    }

    public void showsErrorMessage() {
        assertThat(solo.waitForText("Ошибка"), is(true));
    }

    public void refreshPodcasts() {
        solo.clickOnActionBarItem(R.id.refresh);
    }

    private Matcher<? super Activity> isFinishing() {
        return new TypeSafeMatcher<Activity>() {
            @Override
            protected boolean matchesSafely(Activity activity) {
                return activity.isFinishing();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("an activity that is finishing");
            }

            @Override
            protected void describeMismatchSafely(Activity item, Description mismatchDescription) {
                mismatchDescription
                        .appendText("activity was active [title=")
                        .appendText(item.getTitle().toString())
                        .appendText(", id=")
                        .appendText(item.toString());
            }
        };
    }

    private void podcastList(Matcher<? super ListView> matcher) {
        waitForListToDisplay();
        ListView view = solo.getCurrentListViews().get(0);
        assertThat(view, matcher);
    }

    private void waitForListToDisplay() {
        try {
            Thread.sleep(1000);
            assertThat(solo.waitForView(ListView.class), is(true));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Matcher<? super ListView> has(final Matcher<View> itemMatcher) {
        return new TypeSafeMatcher<ListView>() {
            @Override
            protected boolean matchesSafely(ListView listView) {
                for(int i = 0; i < listView.getCount(); i++) {
                    View item = listView.getChildAt(i);
                    if (itemMatcher.matches(item)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a list item matching");
                itemMatcher.describeTo(description);
            }

            @Override
            protected void describeMismatchSafely(ListView item, Description mismatchDescription) {
                mismatchDescription.appendText("no item found");
            }
        };
    }

    public Matcher<View> anItemWith(String number, String date, String notes) {
        return allOf(
                textField(R.id.podcast_item_view_number, equalTo(number)),
                textField(R.id.podcast_item_view_date, equalTo(date)),
                textField(R.id.podcast_item_view_shownotes, equalTo(notes)));
    }

    private Matcher<? super View> textField(final int id, Matcher<String> matcher) {
        return new FeatureMatcher<View, String>(matcher, "text field", "text field") {
            @Override
            protected String featureValueOf(View container) {
                try {
                    TextView view = (TextView) container.findViewById(id);
                    return (String) view.getText();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    private Matcher<? super ListView> isEmpty() {
        return new FeatureMatcher<ListView, Integer>(equalTo(0),
                "an empty list", "list size") {
            @Override
            protected Integer featureValueOf(ListView listView) {
                return listView.getCount();
            }
        };
    }

}
