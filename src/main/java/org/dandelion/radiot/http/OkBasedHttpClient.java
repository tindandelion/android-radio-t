package org.dandelion.radiot.http;

import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static java.util.Collections.emptyList;

public class OkBasedHttpClient implements HttpClient {

    private static HttpClient instance = new OkBasedHttpClient();

    public static HttpClient instance() {
        return instance;
    }

    protected OkBasedHttpClient() {}

    private final OkHttpClient client = new OkHttpClient.Builder()
            .cookieJar(new AcceptAllCookies())
            .build();

    @Override
    public String getStringContent(String url) throws IOException {
        Response response = executeGetRequest(url);
        return response.body().string();
    }

    @Override
    public byte[] getByteContent(String url) throws IOException {
        Response response = executeGetRequest(url);
        return response.body().bytes();
    }

    @Override
    public void shutdown() {

    }

    private Response executeGetRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return checkHasContent(response);
    }

    private Response checkHasContent(Response response) throws NoContentException {
        if (response.code() == HTTP_NO_CONTENT)
            throw new NoContentException();

        return response;
    }

    private static class AcceptAllCookies implements CookieJar {
        private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            cookieStore.put(url, cookies);
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            if (cookieStore.containsKey(url)) return cookieStore.get(url);
            else return emptyList();
        }
    }
}
