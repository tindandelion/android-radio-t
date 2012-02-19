package org.dandelion.radiot.unittest;

import junit.framework.TestCase;

import org.dandelion.radiot.PodcastItem;

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

	public void testTrimmingShowNotes() throws Exception {
		String notes = "   Note 1 - Note 2   \n\n";
		item.extractShowNotes(notes);
		assertEquals("Note 1 - Note 2", item.getShowNotes());
	}

	public void testOneTag() throws Exception {
		item.addTag("Tag 1");
		assertEquals("Tag 1", item.getTagString());
	}

	public void testManyTags() throws Exception {
		item.addTag("Tag 1");
		item.addTag("Tag 2");
		assertEquals("Tag 1, Tag 2", item.getTagString());
	}

	public void testNoTags() throws Exception {
		assertEquals("", item.getTagString());
	}
}
