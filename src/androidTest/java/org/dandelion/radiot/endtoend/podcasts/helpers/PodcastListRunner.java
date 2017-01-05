package org.dandelion.radiot.endtoend.podcasts.helpers;

import android.app.Activity;
import android.app.Instrumentation;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.robotium.solo.Solo;
import org.dandelion.radiot.R;
import org.dandelion.radiot.podcasts.ui.PodcastListActivity;
import org.hamcrest.Description;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class PodcastListRunner {
    public static final int LIST_DISPLAY_TIMEOUT_MS = 1000;
    private Solo solo;
    private PodcastListActivity activity;

    public PodcastListRunner(Instrumentation instrumentation, PodcastListActivity activity) {
        this.solo = new Solo(instrumentation, activity);
        this.activity = activity;
    }

    public void showsPodcastItem(String number, String date, String description) {
        assertPodcastList(has(anItemWith(number, date, description)));
    }

    public void pressBack() {
        solo.goBack();
    }

    public void wasClosed() {
        assertThat(activity, isFinishing());
    }


    public void showsEmptyList() {
        assertPodcastList(isEmpty());
    }

    public void showsErrorMessage() {
        assertThat("Shows error", solo.waitForText("Ошибка"), is(true));
    }

    public void refreshPodcasts() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.onRefresh.onRefresh();
            }
        });
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

    private void assertPodcastList(Matcher<? super ListView> matcher) {
        try {
            ListView view = waitForListToAppear();
            Thread.sleep(LIST_DISPLAY_TIMEOUT_MS);
            assertThat(view, matcher);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private ListView waitForListToAppear() {
        assertThat(solo.waitForView(ListView.class), is(true));
        return solo.getView(ListView.class, 0);
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
                textField(R.id.number, equalTo(number)),
                textField(R.id.pub_date, equalTo(date)),
                textField(R.id.show_notes, equalTo(notes)));
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
