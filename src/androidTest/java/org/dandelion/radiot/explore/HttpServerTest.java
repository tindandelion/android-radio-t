package org.dandelion.radiot.explore;

import junit.framework.TestCase;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.dandelion.radiot.helpers.HttpServer;

import java.io.IOException;

public class HttpServerTest extends TestCase {
    public static final String URL = HttpServer.addressForUrl("/");
    private MyHttpServer server;
    private DefaultHttpClient client;

    public void testExecuteGetRequest() throws Exception {
        HttpResponse response = sendGetRequest();
        server.hasReceivedRequest("/");
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    public void testReceivesResponseBody() throws Exception {
        HttpResponse response = sendGetRequest();
        String body = EntityUtils.toString(response.getEntity());
        assertEquals(MyHttpServer.BODY_TEXT, body);
    }

    public void testSettingCookies() throws Exception {
        server.setCookie("TestCookie", "TestCookieValue");
        sendGetRequest();

        sendGetRequest();
        String[] expectedCookie = new String[]{"TestCookie", "TestCookieValue"};
        server.hasReceivedRequest("/", HttpServer.NO_PARAMS, expectedCookie);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        server = new MyHttpServer();
        client = new DefaultHttpClient();
    }

    @Override
    public void tearDown() throws Exception {
        server.stop();
        super.tearDown();
    }

    private HttpResponse sendGetRequest() throws IOException {
        return client.execute(new HttpGet(URL));
    }

    private static class MyHttpServer extends HttpServer {
        public static final String BODY_TEXT = "Hello world!";

        public MyHttpServer() throws IOException {
            super();
        }

        @Override
        protected Response serveUri(String uri) {
            return new Response(HTTP_OK, MIME_HTML, BODY_TEXT);
        }
    }
}
