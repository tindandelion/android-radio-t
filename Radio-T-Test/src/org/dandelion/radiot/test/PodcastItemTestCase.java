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
		assertEquals("#100", item.getNumber());
	}

	public void testPodcastNumberInString() throws Exception {
		item.extractPodcastNumber("Radio 100");
		assertEquals("#100", item.getNumber());
	}

	public void testPodcastNumberInLocalizedString() throws Exception {
		item.extractPodcastNumber("Радио-Т 192");
		assertEquals("#192", item.getNumber());
	}

	public void testNumberIsIncorrect() throws Exception {
		item.extractPodcastNumber("Blah");
		assertEquals("Blah", item.getNumber());
	}

	public void testExtractPublicationDate() throws Exception {
		item.extractPubDate("Sun, 13 Jun 2010 01:37:22 +0000");
		assertEquals("13.06.2010", item.getPubDate());
	}
	
	public void testIncorrectPublicationDate() throws Exception {
		item.extractPubDate("Blah");
		assertEquals("", item.getPubDate());
	}
	
	public void testRemoveLineBreaksFromShowNotes() throws Exception {
		String notes = "Line1\nLine2";
		item.extractShowNotes(notes);
		assertEquals("Line1 Line2", item.getShowNotes());
	}

	public void testRemoveHtmlMarkupFromShowNotes() throws Exception {
		String notes = "<p>Line1 <b>bold text</b>";
		item.extractShowNotes(notes);
		assertTrue(item.getShowNotes().contains("Line1 bold text"));
	}
}
