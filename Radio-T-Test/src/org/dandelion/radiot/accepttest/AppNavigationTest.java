package org.dandelion.radiot.accepttest;

import android.test.ActivityInstrumentationTestCase2;
import org.dandelion.radiot.accepttest.drivers.HomeScreenDriver;
import org.dandelion.radiot.home_screen.HomeScreenActivity;

public class AppNavigationTest extends ActivityInstrumentationTestCase2<HomeScreenActivity> {
    private HomeScreenDriver driver;

    public AppNavigationTest() {
        super(HomeScreenActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        driver = new HomeScreenDriver(getInstrumentation(), getActivity());
    }

    public void testNavigateToHomeScreenWhenClickingActionBarTitle() throws Exception {
        driver.goToAboutScreen();
        driver.clickActivityTitle();
        driver.assertOnHomeScreen();
    }
}
