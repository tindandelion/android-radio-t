package org.dandelion.radiot.helpers;

import org.dandelion.radiot.helpers.async.NotificationTrace;
import org.hamcrest.Matcher;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public abstract class HttpServer extends NanoHTTPD {
    public static int PORT = 32768;
    protected NotificationTrace<String> requests;

    public HttpServer() throws IOException {
        super(PORT, new File(""));
        this.requests = new NotificationTrace<String>();
    }


    public static String addressForUrl(String part) {
        return baseUrl() + part;
    }

    public static String baseUrl() {
        return String.format("http://localhost:%d", PORT);
    }

    public void hasReceivedRequest(Matcher<String> matcher) throws InterruptedException {
        requests.receivedNotification(matcher);
    }

    public void hasNotReceivedRequest(Matcher<String> matcher) throws InterruptedException {
        requests.notReceivedNotification(matcher);
    }

    protected abstract Response serveUri(String uri);

    @Override
    public Response serve(String uri, String method, Properties header, Properties parms, Properties files) {
        requests.append(uri);
        return serveUri(uri);
    }
}
