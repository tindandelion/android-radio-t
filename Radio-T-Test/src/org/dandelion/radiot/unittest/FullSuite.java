package org.dandelion.radiot.unittest;

import android.test.suitebuilder.TestSuiteBuilder;
import junit.framework.Test;
import junit.framework.TestSuite;

public class FullSuite extends TestSuite {

	public static Test suite() {
		return new TestSuiteBuilder(FullSuite.class)
				.includeAllPackagesUnderHere().build();
	}

}
