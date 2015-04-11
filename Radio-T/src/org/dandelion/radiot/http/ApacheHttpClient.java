package org.dandelion.radiot.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class ApacheHttpClient implements HttpClient {
    private final static int HTTP_OK = 200;
    private final static int HTTP_NO_CONTENT = 204;


    private DefaultHttpClient client = new DefaultHttpClient();

    public void setReadTimeout(int millis) {
        HttpParams params = client.getParams();
        HttpConnectionParams.setSoTimeout(params, millis);
    }

    @Override
    public String getStringContent(String url) throws IOException {
        HttpEntity entity = executeRequestFor(url);
        return EntityUtils.toString(entity, "utf-8");
    }

    @Override
    public byte[] getByteContent(String fullUrl) throws IOException {
        HttpEntity entity = executeRequestFor(fullUrl);
        return EntityUtils.toByteArray(entity);
    }

    @Override
    public void shutdown() {
        client.getConnectionManager().shutdown();
    }

    private HttpEntity executeRequestFor(String url) throws IOException {
        HttpResponse response = client.execute(new HttpGet(url));
        if (!isSuccessful(response)) {
            return throwException(response);
        }
        return response.getEntity();
    }

    private HttpEntity throwException(HttpResponse response) throws IOException {
        StatusLine sl = response.getStatusLine();
        if (sl.getStatusCode() == HTTP_NO_CONTENT) {
            throw new NoContentException();
        } else {
            throw new IOException(sl.getReasonPhrase());
        }
    }

    private boolean isSuccessful(HttpResponse response) {
        return (response.getStatusLine().getStatusCode() == HTTP_OK) &&
                (response.getEntity() != null);
    }
}
