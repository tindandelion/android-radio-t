package org.dandelion.radiot.podcasts.loader.rss;

import android.sax.*;
import android.util.Xml;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.util.List;

import static java.util.Arrays.asList;

public class RssParser {

    public interface NotesExtractor {
        String extract(String text);
    }

    private static final String itunes = "http://www.itunes.com/dtds/podcast-1.0.dtd";

    private final PodcastList items = new PodcastList();
    private final NotesExtractor notesExtractor;
    private PodcastItem currentItem;

    public RssParser(NotesExtractor notesExtractor) {
        this.notesExtractor = notesExtractor;
    }


    public PodcastList parse(String content) throws SAXException {
        Xml.parse(content, contentHandler());
        return items;
    }

    private ContentHandler contentHandler() {
        RootElement root = new RootElement("rss");
        Element channel = root.getChild("channel");
        Element item = channel.getChild("item");
        currentItem = new PodcastItem();

        item.setEndElementListener(new EndElementListener() {
            public void end() {
                items.add(currentItem);
                currentItem = new PodcastItem();
            }
        });

        item.getChild("title").setEndTextElementListener(
                new EndTextElementListener() {
                    public void end(String body) {
                        currentItem.title = body;
                    }
                });

        item.getChild("pubDate").setEndTextElementListener(
                new EndTextElementListener() {
                    public void end(String body) {
                        currentItem.pubDate = body;
                    }
                });

        item.getChild("enclosure").setStartElementListener(
                enclosureExtractor());

        item.getChild(itunes, "summary")
                .setEndTextElementListener(showNotesExtractor());
        item.getChild(itunes, "image")
                .setStartElementListener(
                        new StartElementListener() {
                            @Override
                            public void start(Attributes attributes) {
                                currentItem.thumbnailUrl = attributes.getValue("href");
                            }
                        }
                );


        return root.getContentHandler();
    }

    private EndTextElementListener showNotesExtractor() {
        return new EndTextElementListener() {
            @Override
            public void end(String s) {
                currentItem.showNotes = notesExtractor.extract(s);
            }
        };
    }

    private StartElementListener enclosureExtractor() {
        final List<String> audioTypes = asList("audio/mp3", "audio/mpeg");

        return new StartElementListener() {
            public void start(Attributes attributes) {
                if (isAudioEnclosure(attributes)) {
                    currentItem.audioUri = attributes.getValue("url");
                }
            }

            private boolean isAudioEnclosure(Attributes attributes) {
                String type = attributes.getValue("type");
                return audioTypes.contains(type);
            }
        };
    }

}
