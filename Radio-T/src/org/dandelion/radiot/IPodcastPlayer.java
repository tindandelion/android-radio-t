package org.dandelion.radiot;

import android.content.Context;
import android.net.Uri;

public interface IPodcastPlayer {
	void startPlaying(Context context, Uri url);
	void stop();
}
