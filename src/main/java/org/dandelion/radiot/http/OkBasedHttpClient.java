package org.dandelion.radiot.http;

import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.emptyList;

public class OkBasedHttpClient implements HttpClient {
    private static final OkHttpClient sharedClient =
            new OkHttpClient.Builder()
                    .cookieJar(new AcceptAllCookies())
                    .build();

    public static HttpClient make() {
        return new OkBasedHttpClient(sharedClient);
    }

    private OkHttpClient client;

    private OkBasedHttpClient(OkHttpClient client) {
        this.client = client;
    }

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
    public void setReadTimeout(int milliseconds) {
        client = client.newBuilder()
                .readTimeout(milliseconds, TimeUnit.MILLISECONDS)
                .build();
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
