package org.dandelion.radiot.accepttest.drivers;

import android.os.Build;
import org.dandelion.radiot.home_screen.AboutAppActivity;
import org.dandelion.radiot.podcasts.ui.PodcastListActivity;
import org.dandelion.radiot.home_screen.HomeScreenActivity;
import android.app.Activity;
import android.app.Instrumentation;

import com.jayway.android.robotium.solo.Solo;

// TODO HomeScreenDriver starts to become a more general driver
public class HomeScreenDriver extends Solo {
    private Instrumentation instrumentation;

    public HomeScreenDriver(Instrumentation inst, Activity activity) {
		super(inst, activity);
        this.instrumentation = inst;
	}

	public void assertOnHomeScreen() {
		assertCurrentActivity("Must be on the home screen", HomeScreenActivity.class);
	}

    public void goToAboutScreen() {
        clickOnText("О программе");
        assertCurrentActivity("Must be on the about screen", AboutAppActivity.class);
    }

    public void goToPodcastsScreen() {
        clickOnText("Подкасты");
        assertCurrentActivity("Must be on the main show screen", PodcastListActivity.class);
    }

    public void goToAfterShowScreen() {
        clickOnText("После-шоу");
        assertCurrentActivity("Must be on the after show page", PodcastListActivity.class);
    }

    // TODO: Get rid of visitMainShowPage()
	public PodcastListActivity visitMainShowPage() {
        goToPodcastsScreen();
		return (PodcastListActivity) getCurrentActivity();
	}


	public void waitSomeTime() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void assertShowingPodcastList() {
		assertCurrentActivity("Must show podcast list", PodcastListActivity.class);
	}

    public void finish() {
        finishOpenedActivities();
    }

    public PodcastListDriver visitMainShowPage2() {
        PodcastListActivity activity = visitMainShowPage();
        return new PodcastListDriver(instrumentation, activity);
    }

    public void clickActivityTitle() {
        ActivityTitleClicker titleClicker = ActivityTitleClicker.create(this);
        titleClicker.click();
    }

}

abstract class ActivityTitleClicker {
    protected HomeScreenDriver driver;

    public static ActivityTitleClicker create(HomeScreenDriver driver) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return new GingerbreadActivityTitleClicker(driver);
        } else {
            return new HoneycombActivityTitleClicker(driver);
        }
    }

    private ActivityTitleClicker(HomeScreenDriver driver) {
        this.driver = driver;
    }

    public abstract void click();

    private static class HoneycombActivityTitleClicker extends ActivityTitleClicker {
        public HoneycombActivityTitleClicker(HomeScreenDriver driver) {
            super(driver);
        }

        @Override
        public void click() {
            driver.clickOnActionBarHomeButton();
        }
    }

    private static class GingerbreadActivityTitleClicker extends ActivityTitleClicker {
        public GingerbreadActivityTitleClicker(HomeScreenDriver driver) {
            super(driver);
        }

        @Override
        public void click() {
            driver.clickOnImageButton(0);
        }
    }
}
