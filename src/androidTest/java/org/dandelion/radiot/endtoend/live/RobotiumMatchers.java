package org.dandelion.radiot.endtoend.live;

import com.robotium.solo.Solo;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class RobotiumMatchers {
    public static final long TIMEOUT_MS = 5000;

    public static Matcher<? super Solo> showsText(final String message) {
        return new TypeSafeMatcher<Solo>() {

            @Override
            protected boolean matchesSafely(Solo solo) {
                return solo.waitForText(message, 1, TIMEOUT_MS);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a text ").appendValue(message);
            }

            @Override
            protected void describeMismatchSafely(Solo item, Description mismatchDescription) {
                mismatchDescription
                        .appendText("text ")
                        .appendValue(message)
                        .appendText(String.format(" didn't show up within %d milliseconds", TIMEOUT_MS));
            }
        };
    }
}
