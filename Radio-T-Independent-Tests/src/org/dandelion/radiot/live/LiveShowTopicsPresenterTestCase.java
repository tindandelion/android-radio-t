package org.dandelion.radiot.live;

import static org.junit.Assert.*;

import org.junit.Test;


public class LiveShowTopicsPresenterTestCase {
	@Test
	public void creation() { 
		LiveShowTopicsPresenter presenter = new LiveShowTopicsPresenter(null);
		assertNotNull(presenter);
	}
}
