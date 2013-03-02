package org.dandelion.radiot.http;

import java.io.IOException;

public interface HttpClient {
    String getStringContent(String url) throws IOException;
}
