package org.dandelion.radiot.home_screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import org.dandelion.radiot.R;

public class HomeScreenActivity extends FragmentActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, HomeScreenActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
    }
}
