package org.dandelion.radiot.unittest;

import org.dandelion.radiot.RadiotApplication;
import org.dandelion.radiot.helpers.MockMediaPlayer;
import org.dandelion.radiot.live.LiveShowService;

import android.app.Application;
import android.content.Intent;
import android.media.MediaPlayer;
import android.test.ServiceTestCase;

public class LiveShowServiceTestCase extends ServiceTestCase<LiveShowService> {

	private LiveShowService service;
	private MockMediaPlayer mockPlayer;

	public LiveShowServiceTestCase() {
		super(LiveShowService.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setApplication(createTestApplication());
		bindService(new Intent());
		service = getService();
	}

	private Application createTestApplication() {
		mockPlayer = new MockMediaPlayer();
		return new RadiotApplication() {
			@Override
			public MediaPlayer getMediaPlayer() {
				return mockPlayer;
			}
		};
	}
}
