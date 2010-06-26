package org.dandelion.radiot.test;

import org.dandelion.radiot.PodcastItem;

import junit.framework.TestCase;

public class PodcastItemTestCase extends TestCase {
	private PodcastItem item;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		item = new PodcastItem();
	}
	
	public void testSimplePodcastNumber() throws Exception {
		item.extractPodcastNumber("100");
		assertEquals(100, item.getNumber());
	}
	
	public void testPodcastNumberInString() throws Exception {
		item.extractPodcastNumber("Radio 100");
		assertEquals(100, item.getNumber());
	}
	
	public void testPodcastNumberInLocalizedString() throws Exception {
		item.extractPodcastNumber("Радио-Т 192");
		assertEquals(192, item.getNumber());
	}
	
	public void testNumberIsIncorrect() throws Exception {
		item.extractPodcastNumber("Blah");
		assertEquals(0, item.getNumber());
	}
	
	public void testExtractPublicationDate() throws Exception {
		item.extractPubDate("Sun, 13 Jun 2010 01:37:22 +0000");
		assertNotNull(item.getPubDate());
	}
}
