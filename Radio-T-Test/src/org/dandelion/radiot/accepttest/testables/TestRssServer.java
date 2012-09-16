package org.dandelion.radiot.accepttest.testables;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class TestRssServer extends NanoHTTPD {
    public TestRssServer() throws IOException {
        super(8080, new File(""));
    }

    @Override
    public Response serve(String uri, String method, Properties header, Properties parms, Properties files) {
        throw new RuntimeException("Reached serve()");
    }

    public void provideRssFeedWithItemCount(int count) {
    }
}
