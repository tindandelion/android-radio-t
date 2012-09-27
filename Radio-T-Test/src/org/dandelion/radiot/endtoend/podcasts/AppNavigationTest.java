package org.dandelion.radiot.endtoend.podcasts;

import android.test.ActivityInstrumentationTestCase2;
import org.dandelion.radiot.accepttest.drivers.AppNavigator;
import org.dandelion.radiot.home_screen.HomeScreenActivity;

public class AppNavigationTest extends ActivityInstrumentationTestCase2<HomeScreenActivity> {
    private AppNavigator driver;

    public AppNavigationTest() {
        super(HomeScreenActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        driver = new AppNavigator(getInstrumentation(), getActivity());
    }

    public void testNavigateToAboutScreenAndBack() throws Exception {
        driver.goToAboutScreen();
        driver.showsAboutSrceen();

        driver.clickActivityTitle();
        driver.showsHomeScreen();
    }

    public void testNavigateToMainShowScreenAndBack() throws Exception {
        driver.goToPodcastsScreen();
        driver.showsPodcastsScreen("Подкасты");

        driver.clickActivityTitle();
        driver.showsHomeScreen();
    }

    public void testNavigateToAfterShowScreenAndBack() throws Exception {
        driver.goToAfterShowScreen();
        driver.showsPodcastsScreen("После-шоу");

        driver.clickActivityTitle();
        driver.showsHomeScreen();
    }
}
