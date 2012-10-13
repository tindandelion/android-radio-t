package org.dandelion.radiot.helpers;

import java.io.File;
import java.io.IOException;

public class HttpServer extends NanoHTTPD {
    public static int PORT = 32768;

    public HttpServer() throws IOException {
        super(PORT, new File(""));
    }

    public static String addressForUrl(String part) {
        return String.format("http://localhost:%d", PORT) + part;
    }
}
