package org.dandelion.radiot.integration;

import junit.framework.TestCase;
import org.dandelion.radiot.helpers.HttpServer;
import org.dandelion.radiot.http.ApacheHttpClient;
import org.dandelion.radiot.http.HttpClient;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class HttpClientTest extends TestCase {
    private static final String URL = HttpServer.addressForUrl("/");
    private final HttpClient client = new ApacheHttpClient();
    private MyHttpServer server;

    public void testExecuteGetRequest() throws Exception {
        client.getStringContent(URL);
        server.hasReceivedRequest("/");
    }

    public void testRequestString() throws Exception {
        String body = client.getStringContent(URL);
        assertThat(body, equalTo(MyHttpServer.BODY_TEXT));
    }

    public void testRequestBytes() throws Exception {
        byte[] expected = MyHttpServer.BODY_TEXT.getBytes();

        byte[] body = client.getByteContent(URL);
        assertThat(body, equalTo(expected));
    }

    public void testSettingCookies() throws Exception {
        server.setCookie("TestCookie", "TestCookieValue");
        client.getStringContent(URL);

        client.getStringContent(URL);
        String[] expectedCookie = {"TestCookie", "TestCookieValue"};
        server.hasReceivedRequest("/", HttpServer.NO_PARAMS, expectedCookie);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        server = new MyHttpServer();
    }

    @Override
    public void tearDown() throws Exception {
        server.stop();
        super.tearDown();
    }

    private static class MyHttpServer extends HttpServer {
        static final String BODY_TEXT = "Hello world!";

        MyHttpServer() throws IOException {
        }

        @Override
        protected Response serveUri(String uri) {
            return new Response(HTTP_OK, MIME_HTML, BODY_TEXT);
        }
    }
}
