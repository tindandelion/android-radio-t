package org.dandelion.radiot.endtoend.podcasts.helpers;

public class RssFeedBuilder {
    private String items = "";

    public static RssFeedBuilder buildFeed() {
        return new RssFeedBuilder();
    }

    public String done() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<rss xmlns:itunes=\"http://www.itunes.com/dtds/podcast-1.0.dtd\">" +
                "<channel>" +
                items +
                "</channel>" +
                "</rss>";
    }

    public RssFeedBuilder item(String content) {
        items += "<item>" + content + "</item>";
        return this;
    }
}
