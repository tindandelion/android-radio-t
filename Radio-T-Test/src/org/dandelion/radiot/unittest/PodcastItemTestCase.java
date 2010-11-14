package org.dandelion.radiot.unittest;

import junit.framework.TestCase;

import org.dandelion.radiot.PodcastItem;
import org.dandelion.radiot.rss.RssEnclosure;
import org.dandelion.radiot.rss.RssItem;

import android.net.Uri;

public class PodcastItemTestCase extends TestCase {
	private PodcastItem item;
	private RssItem rssItem;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		item = new PodcastItem();
		rssItem = new RssItem();
	}

	public void testSimplePodcastNumber() throws Exception {
		rssItem.title = "100";
		item = PodcastItem.fromRss(rssItem);
		assertEquals("#100", item.getNumber());
	}

	public void testPodcastNumberInString() throws Exception {
		rssItem.title = "Radio 100";
		item = PodcastItem.fromRss(rssItem);
		assertEquals("#100", item.getNumber());
	}

	public void testPodcastNumberInLocalizedString() throws Exception {
		rssItem.title = "Радио-Т 100";
		item = PodcastItem.fromRss(rssItem);
		assertEquals("#100", item.getNumber());
	}

	public void testNumberIsIncorrect() throws Exception {
		rssItem.title = "Blah";
		item = PodcastItem.fromRss(rssItem);
		assertEquals("Blah", item.getNumber());
	}

	public void testExtractPublicationDate() throws Exception {
		rssItem.pubDate = "Sun, 13 Jun 2010 01:37:22 +0000";
		item = PodcastItem.fromRss(rssItem);
		assertEquals("13.06.2010", item.getPubDate());
	}

	public void testIncorrectPublicationDate() throws Exception {
		rssItem.pubDate = "Blah";
		item = PodcastItem.fromRss(rssItem);
		assertEquals("", item.getPubDate());
	}

	public void testRemoveLineBreaksFromShowNotes() throws Exception {
		rssItem.description = "Line1\nLine2"; 
		item = PodcastItem.fromRss(rssItem);
		assertEquals("Line1 Line2", item.getShowNotes());
	}

	public void testRemoveHtmlMarkupFromShowNotes() throws Exception {
		rssItem.description = "<p>Line1 <b>bold text</b>"; 
		item = PodcastItem.fromRss(rssItem);
		assertTrue(item.getShowNotes().contains("Line1 bold text"));
	}
	
	public void testPodcastImageUri() throws Exception {
		rssItem.encodedContent = "<img src=\"http://www.image-url.com\" alt=\"\"/>"; 
		item = PodcastItem.fromRss(rssItem);
		assertEquals("http://www.image-url.com", item.getImageUrl());
	}
	
	public void testNoImageUrl() throws Exception {
		rssItem.encodedContent = "bla-bla-bla"; 
		item = PodcastItem.fromRss(rssItem);
		assertNull(item.getImageUrl());
	}

	public void testOneTag() throws Exception {
		rssItem.categories.add("Tag 1");
		item = PodcastItem.fromRss(rssItem);
		assertEquals("Tag 1", item.getTagString());
	}

	public void testManyTags() throws Exception {
		rssItem.categories.add("Tag 1");
		rssItem.categories.add("Tag 2");
		item = PodcastItem.fromRss(rssItem);
		assertEquals("Tag 1, Tag 2", item.getTagString());
	}
	
	public void testPodcastUrl() throws Exception {
		rssItem.enclosures.add(new RssEnclosure("audio/mpeg", "http://podcast-link"));
		item = PodcastItem.fromRss(rssItem);
		assertEquals(Uri.parse("http://podcast-link"), item.getAudioUri());
	}
	
	public void testFilterNonAudioEnclosures() throws Exception {
		rssItem.enclosures.add(new RssEnclosure("audio/mpeg", "http://podcast-link"));
		rssItem.enclosures.add(new RssEnclosure("text/html", "http://other-link"));
		item = PodcastItem.fromRss(rssItem);
		assertEquals(Uri.parse("http://podcast-link"), item.getAudioUri());
	}

	public void testNoTags() throws Exception {
		assertEquals("", item.getTagString());
	}

}
