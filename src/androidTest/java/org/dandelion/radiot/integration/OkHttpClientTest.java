package org.dandelion.radiot.integration;

import junit.framework.TestCase;
import org.dandelion.radiot.helpers.HttpServer;
import org.dandelion.radiot.http.HttpClient;
import org.dandelion.radiot.http.NoContentException;
import org.dandelion.radiot.http.OkBasedHttpClient;

import java.io.IOException;

import static org.dandelion.radiot.helpers.NanoHTTPD.HTTP_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class OkHttpClientTest extends TestCase {
    private static final String URL = HttpServer.addressForUrl("/");
    private final HttpClient client = new OkBasedHttpClient() {};
    private MyHttpServer server;

    public void testExecuteGetRequest() throws Exception {
        server.respondWith(HTTP_OK, "");

        client.getStringContent(URL);
        server.hasReceivedRequest("/");
    }

    public void testGetContent_asString() throws Exception {
        final String expected = "Hello world!";
        server.respondWith(HTTP_OK, expected);

        String body = client.getStringContent(URL);
        assertThat(body, equalTo(expected));
    }

    public void testGetContent_whenNoContent_throwsException() throws Exception {
        server.respondWith("204 No Content", "");
        try {
            client.getStringContent(URL);
            fail("NoContentException expected");
        } catch (NoContentException ignored) {
        }
    }

    public void testGetContent_asBytes() throws Exception {
        final String expected = "Hello world!";
        server.respondWith(HTTP_OK, expected);

        byte[] body = client.getByteContent(URL);
        assertThat(body, equalTo(expected.getBytes()));
    }

    public void testCookies_passingAround() throws Exception {
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
        private Response response = new Response(HTTP_OK, MIME_HTML, BODY_TEXT);

        MyHttpServer() throws IOException {
        }

        @Override
        protected Response serveUri(String uri) {
            return response;
        }

        void respondWith(String status, String body) {
            this.response = new Response(status, MIME_HTML, body);
        }
    }
}
