package org.dandelion.radiot.helpers;

import java.io.IOException;

import junit.framework.Assert;
import android.media.MediaPlayer;

public class MockMediaPlayer extends MediaPlayer {

	enum State {
		Idle, Preparing, Playing, Prepared
	}

	private String dataSource;
	private OnPreparedListener onPreparedListener;
	private State currentState = State.Idle;
	private IOException connectionError;
	private OnErrorListener onErrorListener;

	@Override
	public void setDataSource(String path) throws IOException,
			IllegalArgumentException, IllegalStateException {
		if (null != connectionError) {
			throw connectionError;
		}
		dataSource = path;
	}

	@Override
	public void setOnErrorListener(OnErrorListener listener) {
		onErrorListener = listener;
	}

	public void prepareError() {
		onErrorListener.onError(this, MEDIA_ERROR_UNKNOWN, 0);
	}

	public void throwsConnectionError() {
		connectionError = new IOException();
	}

	public void assertIsReset() {
		Assert.assertEquals(State.Idle, currentState);
	}

	public void assertIsPlaying(String expectedUrl) {
		Assert.assertEquals(State.Playing, currentState);
		Assert.assertEquals(expectedUrl, dataSource);
	}

	@Override
	public boolean isPlaying() {
		return State.Playing == currentState;
	}

	public void bePrepared() {
		currentState = State.Prepared;
		if (null != onPreparedListener) {
			onPreparedListener.onPrepared(this);
		}
	}

	@Override
	public void setOnPreparedListener(OnPreparedListener listener) {
		onPreparedListener = listener;
	}

	@Override
	public void prepareAsync() throws IllegalStateException {
		checkState(State.Idle);
		currentState = State.Preparing;
	}

	@Override
	public void start() throws IllegalStateException {
		checkState(State.Prepared);
		currentState = State.Playing;
	}

	private void checkState(State expected) {
		Assert.assertEquals(expected, currentState);
	}

	@Override
	public void reset() {
		currentState = State.Idle;
		dataSource = null;
	}
}