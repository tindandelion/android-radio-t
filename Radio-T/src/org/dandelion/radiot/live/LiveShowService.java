package org.dandelion.radiot.live;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class LiveShowService extends Service {
	private final IBinder binder = new LocalBinder();

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("RadioT", "Playback service created!");
	}
	
	@Override
	public void onDestroy() {
		Log.i("RadioT", "Playback service destroyed!");
		super.onDestroy();
	}
	
	public class LocalBinder extends Binder {
		LiveShowService getService() {
			return (LiveShowService.this);
		}
	}
}
