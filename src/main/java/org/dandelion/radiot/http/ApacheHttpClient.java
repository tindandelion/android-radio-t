package org.dandelion.radiot.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class ApacheHttpClient implements HttpClient {
    private final static int HTTP_OK = 200;

    private static final ResponseHandler<byte[]> responseHandler = new ResponseHandler<byte[]>() {
        @Override
        public byte[] handleResponse(HttpResponse response) throws IOException {
            if (!isSuccessful(response)) {
                throwException(response);
            }
            HttpEntity entity = response.getEntity();
            return EntityUtils.toByteArray(entity);
        }

        private boolean isSuccessful(HttpResponse response) {
            return (response.getStatusLine().getStatusCode() == HTTP_OK) &&
                    (response.getEntity() != null);
        }

        private void throwException(HttpResponse response) throws IOException {
            StatusLine sl = response.getStatusLine();
            if (sl.getStatusCode() == HTTP_NO_CONTENT) {
                throw new NoContentException();
            } else {
                throw new IOException(sl.getReasonPhrase());
            }
        }
    };


    private DefaultHttpClient client = new DefaultHttpClient();

    public void setReadTimeout(int millis) {
        HttpParams params = client.getParams();
        HttpConnectionParams.setSoTimeout(params, millis);
    }

    @Override
    public String getStringContent(String url) throws IOException {
        byte[] content = getByteContent(url);
        return new String(content, "utf-8");
    }

    @Override
    public byte[] getByteContent(String url) throws IOException {
        return client.execute(new HttpGet(url), responseHandler);
    }

    @Override
    public void shutdown() {
        client.getConnectionManager().shutdown();
    }

}
