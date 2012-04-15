package org.dandelion.radiot.home_screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import org.dandelion.radiot.R;
import org.dandelion.radiot.live.ui.LiveShowActivity;
import org.dandelion.radiot.podcasts.ui.PodcastListActivity;
import org.dandelion.radiot.util.CustomTitleActivity;

public class HomeScreenActivity extends CustomTitleActivity {
    public static void start(Context context) {
        Intent intent = new Intent(context, HomeScreenActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        initDashboardItems();
    }

    private void initDashboardItems() {
        onButtonClick(R.id.home_btn_main_show, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PodcastListActivity.start(HomeScreenActivity.this,
                        getString(R.string.main_show_title), "main-show");
            }
        });
        onButtonClick(R.id.home_btn_after_show, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PodcastListActivity.start(HomeScreenActivity.this,
                        getString(R.string.after_show_title), "after-show");
            }
        });
        onButtonClick(R.id.home_btn_live_show, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeScreenActivity.this,
                        LiveShowActivity.class));
            }
        });
        onButtonClick(R.id.home_btn_about, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeScreenActivity.this,
                        AboutAppActivity.class));
            }
        });
    }

    private void onButtonClick(int id, View.OnClickListener listener) {
        findViewById(id).setOnClickListener(listener);
    }

    protected void setupTitleBar() {
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.home_screen_titlebar);
    }
}
