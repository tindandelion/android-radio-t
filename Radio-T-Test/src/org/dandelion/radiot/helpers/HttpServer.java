package org.dandelion.radiot.helpers;

import org.dandelion.radiot.helpers.async.NotificationTrace;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import static org.dandelion.radiot.helpers.HttpServer.RequestMatcher.atUrl;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.equalTo;

public abstract class HttpServer extends NanoHTTPD {
    public static int PORT = 32768;
    public static final String NO_PARAMS = "";

    protected NotificationTrace<ReceivedRequest> requests;
    private String[] cookie = null;

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
        requests.receivedNotification(atUrl(equalTo(url), anyParams(), anyParams()));
    }

    public void hasReceivedRequest(String url, String params) throws InterruptedException {
        requests.receivedNotification(atUrl(equalTo(url), withParams(params), anyParams()));
    }

    public void hasReceivedRequest(String url, String params, Properties cookies) throws InterruptedException {
        requests.receivedNotification(atUrl(equalTo(url), withParams(params), withCookies(cookies)));
    }

    private Matcher<Properties> withCookies(Properties cookies) {
        return equalTo(cookies);
    }

    private Matcher<Properties> anyParams() {
        return any(Properties.class);
    }

    private Matcher<Properties> withParams(String urlParams) {
        return equalTo(toProperties(urlParams));
    }

    private Properties toProperties(String urlParams) {
        Properties result = new Properties();
        if (urlParams.equals(NO_PARAMS)) {
            return result;
        }

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
        requests.append(new ReceivedRequest(uri, parms, extractCookies(header)));
        return addCookiesTo(serveUri(uri));
    }

    private Properties extractCookies(Properties header) {
        final String COOKIE_KEY = "cookie";
        if (header.containsKey(COOKIE_KEY)) {
            String cookieStr = header.getProperty(COOKIE_KEY);
            return toProperties(cookieStr);
        }
        return new Properties();
    }

    public void setCookie(String name, String value) {
        cookie = new String[] {name, value};
    }

    private Response addCookiesTo(Response response) {
        if (cookie != null) {
            response.addHeader("Set-Cookie", String.format("%s=%s", cookie[0], cookie[1]));
        }
        return response;
    }



    private static class ReceivedRequest {
        public final String url;
        public final Properties parameters;
        public final Properties cookies;

        private ReceivedRequest(String url, Properties parms, Properties cookies) {
            this.url = url;
            this.parameters = parms;
            this.cookies = cookies;
        }

        @Override
        public String toString() {
            return String.format("ReceivedRequest(url=%s, params=%s, cookies=%s)", url, parameters, cookies);
        }
    }

    public static class RequestMatcher extends TypeSafeMatcher<ReceivedRequest> {
        private final Matcher<String> urlMatcher;
        private final Matcher<Properties> paramMatcher;
        private final Matcher<Properties> cookieMatcher;

        public static Matcher<? super ReceivedRequest> atUrl(Matcher<String> urlMatcher, Matcher<Properties> paramMatcher, Matcher<Properties> cookieMatcher) {
            return new RequestMatcher(urlMatcher, paramMatcher, cookieMatcher);
        }

        public RequestMatcher(Matcher<String> urlMatcher, Matcher<Properties> paramMatcher, Matcher<Properties> cookieMatcher) {
            this.urlMatcher = urlMatcher;
            this.paramMatcher = paramMatcher;
            this.cookieMatcher = cookieMatcher;
        }

        @Override
        protected boolean matchesSafely(ReceivedRequest request) {
            return urlMatcher.matches(request.url) &&
                    paramMatcher.matches(request.parameters) &&
                    cookieMatcher.matches(request.cookies);
        }

        @Override
        public void describeTo(Description description) {
            description
                    .appendText("a request with url ")
                    .appendDescriptionOf(urlMatcher)
                    .appendText(", parameters ")
                    .appendDescriptionOf(paramMatcher)
                    .appendText(", cookies ")
                    .appendDescriptionOf(cookieMatcher);
        }

    }
}
