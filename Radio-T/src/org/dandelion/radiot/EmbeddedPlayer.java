package org.dandelion.radiot;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Toast;

public class EmbeddedPlayer extends MediaPlayer implements IPodcastPlayer {
	public void startPlaying(Context context, Uri url) {
		try {
			setDataSource(context, url);
			setOnPreparedListener(new OnPreparedListener() {
				public void onPrepared(MediaPlayer mp) {
					mp.start();
				}
			});
			prepareAsync();
		} catch (Exception e) {
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
}
