package org.dandelion.radiot.helpers;

import org.dandelion.radiot.helpers.async.NotificationTrace;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.dandelion.radiot.helpers.HttpServer.RequestMatcher.atUrl;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.equalTo;

public abstract class HttpServer extends NanoHTTPD {
    public static int PORT = 32768;
    protected final Map<String, String> cookies = new HashMap<String, String>();
    protected NotificationTrace<ReceivedRequest> requests;

    public HttpServer() throws IOException {
        super(PORT, new File(""));
        this.requests = new NotificationTrace<ReceivedRequest>();
    }


    public static String addressForUrl(String part) {
        return baseUrl() + part;
    }

    public static String baseUrl() {
        return String.format("http://localhost:%d", PORT);
    }

    public void hasReceivedRequest(String url) throws InterruptedException {
        requests.receivedNotification(atUrl(equalTo(url), any(Properties.class)));
    }

    public void hasReceivedRequest(String url, String params) throws InterruptedException {
        requests.receivedNotification(atUrl(equalTo(url), withParams(params)));
    }

    private Matcher<Properties> withParams(String urlParams) {
        return equalTo(toProperties(urlParams));
    }

    private Properties toProperties(String urlParams) {
        Properties result = new Properties();
        String[] params = urlParams.split("&");
        for (String param : params) {
            String[] pair = param.split("=");
            result.put(pair[0], pair[1]);
        }
        return result;
    }

    public void hasNotReceivedAnyRequest() throws InterruptedException {
        requests.notReceivedNotification(any(ReceivedRequest.class));
    }

    protected abstract Response serveUri(String uri);

    @Override
    public Response serve(String uri, String method, Properties header, Properties parms, Properties files) {
        requests.append(new ReceivedRequest(uri, parms));
        return addCookiesTo(serveUri(uri));
    }

    public void setCookie(String name, String value) {
        cookies.put(name, value);
    }

    private Response addCookiesTo(Response response) {
        for (String name : cookies.keySet()) {
            String value = cookies.get(name);
            response.addHeader("Set-Cookie", String.format("%s=%s", name, value));
        }
        return response;
    }


    private static class ReceivedRequest {
        public final String url;
        private final Properties parameters;

        private ReceivedRequest(String url, Properties parms) {
            this.url = url;
            this.parameters = parms;
        }

        @Override
        public String toString() {
            return String.format("ReceivedRequest(url=%s, params=%s)", url, parameters);
        }
    }

    public static class RequestMatcher extends TypeSafeMatcher<ReceivedRequest> {
        private final Matcher<String> urlMatcher;
        private final Matcher<Properties> paramMatcher;

        public static Matcher<? super ReceivedRequest> atUrl(Matcher<String> urlMatcher, Matcher<Properties> paramMatcher) {
            return new RequestMatcher(urlMatcher, paramMatcher);
        }

        public RequestMatcher(Matcher<String> urlMatcher, Matcher<Properties> paramMatcher) {
            this.urlMatcher = urlMatcher;
            this.paramMatcher = paramMatcher;
        }

        @Override
        protected boolean matchesSafely(ReceivedRequest request) {
            return urlMatcher.matches(request.url) &&
                    paramMatcher.matches(request.parameters);
        }

        @Override
        public void describeTo(Description description) {
            description
                    .appendText("a request with url ")
                    .appendDescriptionOf(urlMatcher)
                    .appendText(" and parameters ")
                    .appendDescriptionOf(paramMatcher);
        }

    }
}
