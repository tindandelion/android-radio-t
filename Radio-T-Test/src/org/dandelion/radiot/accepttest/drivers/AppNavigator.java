package org.dandelion.radiot.accepttest.drivers;

import android.os.Build;
import org.dandelion.radiot.home_screen.AboutAppActivity;
import org.dandelion.radiot.podcasts.ui.PodcastListActivity;
import org.dandelion.radiot.home_screen.HomeScreenActivity;
import android.app.Activity;
import android.app.Instrumentation;

import com.jayway.android.robotium.solo.Solo;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class AppNavigator extends Solo {

    public AppNavigator(Instrumentation inst, Activity activity) {
		super(inst, activity);
	}

	public void showsHomeScreen() {
		assertCurrentActivity("Must be on the home screen", HomeScreenActivity.class);
	}

    public void showsAboutSrceen() {
        assertCurrentActivity("Must be on the about screen", AboutAppActivity.class);
    }

    public void goToAboutScreen() {
        clickOnText("О программе");
    }

    public void goToPodcastsScreen() {
        clickOnText("Выпуски");
    }

    public void showsPodcastsScreen(CharSequence title) {
        assertCurrentActivity("Must be on the main show screen", PodcastListActivity.class);
        assertThat(getCurrentActivity().getTitle(),
                equalTo(title));
    }


    public void goToAfterShowScreen() {
        clickOnText("После-шоу");
        assertCurrentActivity("Must be on the after show page", PodcastListActivity.class);
    }


    public void clickActivityTitle() {
        ActivityTitleClicker titleClicker = ActivityTitleClicker.create(this);
        titleClicker.click();
    }

}

abstract class ActivityTitleClicker {
    protected AppNavigator driver;

    public static ActivityTitleClicker create(AppNavigator driver) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return new GingerbreadActivityTitleClicker(driver);
        } else {
            return new HoneycombActivityTitleClicker(driver);
        }
    }

    private ActivityTitleClicker(AppNavigator driver) {
        this.driver = driver;
    }

    public abstract void click();

    private static class HoneycombActivityTitleClicker extends ActivityTitleClicker {
        public HoneycombActivityTitleClicker(AppNavigator driver) {
            super(driver);
        }

        @Override
        public void click() {
            driver.clickOnActionBarHomeButton();
        }
    }

    private static class GingerbreadActivityTitleClicker extends ActivityTitleClicker {
        public GingerbreadActivityTitleClicker(AppNavigator driver) {
            super(driver);
        }

        @Override
        public void click() {
            driver.clickOnImageButton(0);
        }
    }
}
