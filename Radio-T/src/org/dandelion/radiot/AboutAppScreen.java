package org.dandelion.radiot;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AboutAppScreen extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_app);
	}

	public void sendFeedback(View view) {
		Toast.makeText(this, "Sending feedback", Toast.LENGTH_SHORT).show();
	}
}
