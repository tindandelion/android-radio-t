package org.dandelion.radiot.rss;

import java.io.IOException;
import java.io.InputStream;

public interface IFeedSource {
	InputStream openFeedStream() throws IOException;
}