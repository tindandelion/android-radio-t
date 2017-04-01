package org.dandelion.radiot.podcasts.loader.rss;

import android.text.Html;
import android.text.Spanned;

public class HtmlFormatNotesExtractor implements RssParser.NotesExtractor {

    @Override
    public String extract(String text) {
        Spanned spanned = Html.fromHtml(text, null, null);
        return trimBlanks(removeLineBreaks(removeImageMarkers(spanned.toString())));
    }

    private String removeLineBreaks(String s) {
        return s.replaceAll("\n", " ");
    }

    private String removeImageMarkers(String s) {
        final String imgMarker = "\uFFFC";
        return s.replaceAll(imgMarker, "");
    }

    private String trimBlanks(String s) {
        return s.replaceAll("\\s*$|^\\s*", "");
    }
}
