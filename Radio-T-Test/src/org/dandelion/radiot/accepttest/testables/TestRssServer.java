package org.dandelion.radiot.accepttest.testables;

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

    public void provideRssFeedWithItemCount(int count) {
        RssFeedBuilder builder = new RssFeedBuilder();
        for(int i = 0; i < count; i++) {
            builder.newRssItem();
        }
        rssContent = builder.build();
    }

    public void provideEmptyRssFeed() {
        rssContent = new RssFeedBuilder().build();
    }

    private static class RssFeedBuilder {
        private String items = "";
        public String build() {
            return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<rss>" +
                    "<channel>" +
                    items +
                    "</channel>" +
                    "</rss>";
        }

        public void newRssItem() {
            items += "<item></item>";
        }
    }
}
