package org.dandelion.radiot.unittest;

import junit.framework.TestCase;
import org.dandelion.radiot.podcasts.loader.rss.HtmlFormatNotesExtractor;

public class HtmlFormatNotesExtractorTest extends TestCase {

    public void test_extractSimpleText_makesNoChanges() throws Exception {
        HtmlFormatNotesExtractor extractor = new HtmlFormatNotesExtractor();
        String text = "very simple text";

        assertEquals(text, extractor.extract(text));
    }

    public void test_extractFromHtml_omitsHtmlTags() throws Exception {
        HtmlFormatNotesExtractor extractor = new HtmlFormatNotesExtractor();
        String text = "<p>html-formatted <a href=\"http://google.fi\">text</a>";

        assertEquals("html-formatted text", extractor.extract(text));
    }
}
