package org.dandelion.radiot.helpers;

import android.test.ActivityInstrumentationTestCase2;
import org.dandelion.radiot.accepttest.drivers.AppNavigator;
import org.dandelion.radiot.home_screen.HomeScreenActivity;

// TODO: Remove this class
public class PodcastListAcceptanceTestCase extends
        ActivityInstrumentationTestCase2<HomeScreenActivity> {

    public PodcastListAcceptanceTestCase() {
        super(HomeScreenActivity.class);
    }

    protected AppNavigator createDriver() {
        return new AppNavigator(getInstrumentation(), getActivity());
    }

}