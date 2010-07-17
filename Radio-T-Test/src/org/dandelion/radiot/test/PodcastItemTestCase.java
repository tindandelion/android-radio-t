package org.dandelion.radiot.test;

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

	public void testPodcastImageUri() throws Exception {
		item.extractImageUrl("<img src=\"http://www.image-url.com\" alt=\"\"/>");
		assertEquals("http://www.image-url.com", item.getImageUrl());
	}

	public void testNoImageUrl() throws Exception {
		item.extractImageUrl("bla-bla");
		assertNull(item.getImageUrl());
	}
}
