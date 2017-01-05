package org.dandelion.radiot.http;

import java.io.IOException;

public interface HttpClient {
    public static int HTTP_NO_CONTENT = 204;

    String getStringContent(String url) throws IOException;
    byte[] getByteContent(String url) throws IOException;
    void shutdown();
}
