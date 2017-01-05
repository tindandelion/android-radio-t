package org.dandelion.radiot.integration;

import junit.framework.TestCase;
import org.dandelion.radiot.podcasts.loader.rss.HtmlFormatNotesExtractor;

public class HtmlFormatNotesExtractorTest extends TestCase {
    private final HtmlFormatNotesExtractor extractor = new HtmlFormatNotesExtractor();

    public void test_extractSimpleText_makesNoChanges() throws Exception {
        String text = "very simple text";
        assertEquals(text, extractor.extract(text));
    }

    public void test_removeLeadingAndTrainingBlanks() throws Exception {
        String text = "   text with blanks   ";
        assertEquals("text with blanks", extractor.extract(text));
    }

    public void test_extractFromHtml_omitsHtmlFormattingTags() throws Exception {
        String text = "<p>html-formatted <a href=\"http://google.fi\">text</a>";
        assertEquals("html-formatted text", extractor.extract(text));
    }


    public void test_extractFromHtml_withImages() throws Exception {
        String text = "<img src=\"https://radio-t.com/images/radio-t/rt526.jpg\" alt=\"\">text";
        assertEquals("text", extractor.extract(text));
    }

    public void test_extractFromHtml_listItems() throws Exception {
        String text = "<li>item1</li><li>item2</li>";
        assertEquals("● item1 ● item2", extractor.extract(text));
    }

    public void test_replaceNewLines_withTwoSpaces() throws Exception {
        String text = "<ul><li>line1</li></ul><p>line2</p>";
        assertEquals("● line1  line2", extractor.extract(text));
    }
}
