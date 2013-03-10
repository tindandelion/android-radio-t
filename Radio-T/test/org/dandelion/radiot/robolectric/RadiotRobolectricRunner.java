package org.dandelion.radiot.robolectric;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.runners.model.InitializationError;

import java.io.File;

public class RadiotRobolectricRunner extends RobolectricTestRunner {
    public RadiotRobolectricRunner(Class<?> testClass) throws InitializationError {
        super(testClass, new File("Radio-T"));
    }
}
