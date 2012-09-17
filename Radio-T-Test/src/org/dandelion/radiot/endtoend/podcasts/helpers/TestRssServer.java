package org.dandelion.radiot.endtoend.podcasts.helpers;

import org.dandelion.radiot.helpers.SyncValueHolder;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class TestRssServer extends NanoHTTPD {
    private SyncValueHolder<Object> requestHolder = new SyncValueHolder<Object>();
    private SyncValueHolder<String> responseHolder = new SyncValueHolder<String>();

    public TestRssServer() throws IOException {
        super(8080, new File(""));
    }

    @Override
    public Response serve(String uri, String method, Properties header, Properties parms, Properties files) {
        requestHolder.setValue(new Object());
        return new Response(HTTP_OK, MIME_XML, responseHolder.getValue());
    }

    public void hasReceivedRequest() {
        requestHolder.getValue();
    }

    public void respondWith(String response) {
        responseHolder.setValue(response);
    }

    @Override
    public void stop() {
        responseHolder.setValue("");
        super.stop();
    }
}
