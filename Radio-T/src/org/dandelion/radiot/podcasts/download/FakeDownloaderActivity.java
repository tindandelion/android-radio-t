package org.dandelion.radiot.podcasts.download;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import org.dandelion.radiot.R;

public class FakeDownloaderActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_unsupported_screen);
    }
    
    public void sendFeedback(View v) {

    }
}