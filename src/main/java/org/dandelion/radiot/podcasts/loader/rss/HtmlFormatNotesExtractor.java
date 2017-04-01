package org.dandelion.radiot.podcasts.loader.rss;

import android.text.Html;
import android.text.Spanned;

public class HtmlFormatNotesExtractor implements RssParser.NotesExtractor {

    private static final Html.TagHandler tagHandler = (opening, tag, output, xmlReader) -> {
        if (opening && "li".equals(tag)) {
            output.append(" ● ");
        }
    };

    @Override
    public String extract(String text) {
        Spanned spanned = Html.fromHtml(text, null, tagHandler);
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
