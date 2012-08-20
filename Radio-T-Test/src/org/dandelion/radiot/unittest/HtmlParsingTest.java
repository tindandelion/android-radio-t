package org.dandelion.radiot.unittest;

import android.graphics.drawable.Drawable;
import android.test.AndroidTestCase;
import android.text.Html;
import android.text.Spanned;

public class HtmlParsingTest extends AndroidTestCase {
    public static final String IMG_MARK_SYMBOL = "\ufffc";

    public void testParseEmptyString() throws Exception {
        String htmlToParse = "";
        assertEquals("", parseHtml(htmlToParse));
    }

    public void testParseSimpleString() throws Exception {
        String htmlToParse = "Test";
        assertEquals("Test", parseHtml(htmlToParse));
    }

    public void testParseStringWithParagraphs() throws Exception {
        String htmlToParse = "<p>Test1</p><p>Test2</p>";
        assertEquals("Test1\n\nTest2\n\n", parseHtml(htmlToParse));
    }

    public void testParseStringWithLink() throws Exception {
        String htmlToParse = "<a href=\"http://google.com\">Test</a>";
        assertEquals("Test", parseHtml(htmlToParse));
    }

    public void testExtractingTextFromSpanned() throws Exception {
        String htmlToParse = "<img src=\"http://google.com\" />Test";
        assertEquals("Test", parseHtml(htmlToParse));
    }


    public void testParseStringWithMultipleImages() throws Exception {
        String htmlToParse = "<img src=\"http://google.com\"/>Test<img src=\"http://google.com\"/>";
        assertEquals("Test", parseHtml(htmlToParse));
    }

    public void testParseStringWithCustomImageHandler() throws Exception {
        String htmlToParse = "<img src=\"http://google.com\"/>Test";
        FakeImageGetter imageGetter = new FakeImageGetter();
        parseHtml(htmlToParse, imageGetter);
        assertEquals("http://google.com", imageGetter.requestedUrl);
    }

    public void testParseUnorderedList() throws Exception {
        String htmlToParse = "<ul><li>Line1</li><li>Line2</li></ul>";
        assertEquals("Line1Line2", parseHtml(htmlToParse));
    }

    private String parseHtml(String htmlToParse) {
        return parseHtml(htmlToParse, null);
    }

    private String parseHtml(String htmlToParse, Html.ImageGetter imageGetter) {
        Spanned spanned = Html.fromHtml(htmlToParse, imageGetter, null);
        return spanned.toString().replace(IMG_MARK_SYMBOL, "");
    }

    class FakeImageGetter implements Html.ImageGetter {
        public String requestedUrl = null;

        @Override
        public Drawable getDrawable(String url) {
            requestedUrl = url;
            return null;
        }
    }
}
