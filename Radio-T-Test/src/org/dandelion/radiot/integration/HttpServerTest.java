package org.dandelion.radiot.integration;

import junit.framework.TestCase;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.dandelion.radiot.helpers.NanoHTTPD;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class HttpServerTest extends TestCase {
    public static final String URL = HttpServer.addressForUrl("/");
    private HttpServer server;
    private DefaultHttpClient client;

    public void testExecuteGetRequest() throws Exception {
        HttpResponse response = client.execute(new HttpGet(URL));
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    public void testReceivesResponseBody() throws Exception {
        HttpResponse response = client.execute(new HttpGet(URL));
        String body = EntityUtils.toString(response.getEntity());
        assertEquals(HttpServer.BODY_TEXT, body);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        server = new HttpServer();
        client = new DefaultHttpClient();
    }

    @Override
    public void tearDown() throws Exception {
        server.stop();
        super.tearDown();
    }

    private static class HttpServer extends NanoHTTPD {
        public static int PORT = 32768;
        public static final String BODY_TEXT = "Hello world!";

        public static String addressForUrl(String part) {
            return String.format("http://localhost:%d", PORT) + part;
        }

        public HttpServer() throws IOException {
            super(PORT, new File(""));
        }

        @Override
        public Response serve(String uri, String method, Properties header, Properties parms, Properties files) {
            return new Response(HTTP_OK, MIME_HTML, BODY_TEXT);
        }
    }
}
