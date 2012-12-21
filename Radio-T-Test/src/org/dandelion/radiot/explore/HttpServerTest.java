package org.dandelion.radiot.explore;

import junit.framework.TestCase;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.dandelion.radiot.helpers.HttpServer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<Cookie> cookies = client.getCookieStore().getCookies();
        assertEquals(1, cookies.size());

        Cookie cookie = cookies.get(0);
        assertEquals("TestCookie", cookie.getName());
        assertEquals("TestCookieValue", cookie.getValue());
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

        private final Map<String, String> cookies = new HashMap<String, String>();

        public MyHttpServer() throws IOException {
            super();
        }

        @Override
        protected Response serveUri(String uri) {
            return createResponse();
        }

        private Response createResponse() {
            Response response = new Response(HTTP_OK, MIME_HTML, BODY_TEXT);
            addCookiesTo(response);
            return response;
        }

        private void addCookiesTo(Response response) {
            for (String name : cookies.keySet()) {
                String value = cookies.get(name);
                response.addHeader("Set-Cookie", String.format("%s=%s", name, value));
            }
        }

        public void setCookie(String name, String value) {
            cookies.put(name, value);
        }
    }
}
