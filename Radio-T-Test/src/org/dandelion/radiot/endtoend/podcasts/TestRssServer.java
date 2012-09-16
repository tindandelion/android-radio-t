package org.dandelion.radiot.endtoend.podcasts;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class TestRssServer extends NanoHTTPD {
    private String rssContent = "";

    public TestRssServer() throws IOException {
        super(8080, new File(""));
    }

    @Override
    public Response serve(String uri, String method, Properties header, Properties parms, Properties files) {
        return new Response(HTTP_OK, MIME_XML, rssContent);
    }

    public void setContent(String value) {
        rssContent = value;
    }

    public RssFeedBuilder buildFeed() {
        return new RssFeedBuilder(this);
    }

    public static class RssFeedBuilder {
        private String items = "";
        private TestRssServer server;

        public RssFeedBuilder(TestRssServer server) {
            this.server = server;
        }

        public void done() {
            String result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<rss xmlns:itunes=\"http://www.itunes.com/dtds/podcast-1.0.dtd\">" +
                    "<channel>" +
                    items +
                    "</channel>" +
                    "</rss>";
            server.setContent(result);
        }

        public RssFeedBuilder empty() {
            items = "";
            return this;
        }

        public RssFeedBuilder item(String content) {
            items += "<item>" + content + "</item>";
            return this;
        }

        public RssFeedBuilder items(int count) {
            for(int i = 0; i < count; i++) {
                item("");
            }
            return this;
        }
    }

}
