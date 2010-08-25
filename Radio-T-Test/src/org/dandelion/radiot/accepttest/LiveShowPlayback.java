package org.dandelion.radiot.accepttest;

import org.dandelion.radiot.live.LiveShowActivity;

import com.jayway.android.robotium.solo.Solo;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;

public class LiveShowPlayback extends
		ActivityInstrumentationTestCase2<LiveShowActivity> {

	private LiveShowActivity activity;
	private Solo solo;
	private MediaPlayer player;

	public LiveShowPlayback() {
		super("org.dandelion.radiot", LiveShowActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
		solo = new Solo(getInstrumentation(), activity);
	}

	public void testStartLivePlayback() throws Exception {
		solo.clickOnButton("Start");
		assertTrue(solo.waitForText("Playing"));
	}
}
