package org.dandelion.radiot.podcasts.loader.rss;

import android.text.Html;

public class HtmlFormatNotesExtractor implements RssParser.NotesExtractor {
    @Override
    public String extract(String text) {
        return trimBlanks(Html.fromHtml(text).toString());
    }

    private String trimBlanks(String s) {
        return s.replaceAll("\\s*$", "");
    }
}
