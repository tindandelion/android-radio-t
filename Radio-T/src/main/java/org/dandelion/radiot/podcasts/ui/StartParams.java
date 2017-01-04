package org.dandelion.radiot.podcasts.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

class StartParams {
    private static final String TITLE_EXTRA = "title";
    private static final String SHOW_NAME_EXTRA = "podcast_url";

    private Bundle extras;

    public static Intent createIntent(Context context, String title, String showName) {
        Intent intent = new Intent(context, PodcastListActivity.class);
        intent.putExtra(SHOW_NAME_EXTRA, showName);
        intent.putExtra(TITLE_EXTRA, title);
        return intent;
    }

    public static StartParams fromIntent(Intent intent) {
        return new StartParams(intent.getExtras());
    }

    StartParams(Bundle extras) {
        this.extras = extras;
    }

    public String title() {
        return getStringValue(TITLE_EXTRA, "");
    }

    public String showName() {
        return getStringValue(SHOW_NAME_EXTRA, "");
    }

    private String getStringValue(String key, String defaultValue) {
        if (null == extras) {
            return defaultValue;
        }
        return extras.getString(key);
    }
}
