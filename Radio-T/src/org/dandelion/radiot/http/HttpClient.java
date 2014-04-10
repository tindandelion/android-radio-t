package org.dandelion.radiot.http;

import java.io.IOException;

public interface HttpClient {
    String getStringContent(String url) throws IOException;
    byte[] getByteContent(String url) throws IOException;
    void shutdown();
}
