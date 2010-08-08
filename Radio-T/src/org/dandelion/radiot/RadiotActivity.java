package org.dandelion.radiot;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.view.Window;
import android.view.WindowManager;

public class RadiotActivity extends Activity {

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
		window.addFlags(WindowManager.LayoutParams.FLAG_DITHER);
	}
	
}
