package org.dandelion.radiot.accepttest.drivers;

import android.app.Instrumentation;
import android.app.ListActivity;
import android.widget.ListAdapter;
import com.jayway.android.robotium.solo.Solo;
import org.dandelion.radiot.podcasts.core.PodcastItem;

public class PodcastListDriver extends Solo {
    private ListActivity listActivity;

    public PodcastListDriver(Instrumentation instrumentation, ListActivity activity) {
        super(instrumentation, activity);
        this.listActivity = activity;
    }
    
    public PodcastItem clickOnPodcastItem(int index) {
        ListAdapter adapter = listActivity.getListAdapter();
        PodcastItem item = (PodcastItem) adapter.getItem(index);
        clickInList(index);
        return item;
    }
    
    public PodcastItem selectItemForPlaying(int index) {
        PodcastItem item = clickOnPodcastItem(index);
        clickOnText("Играть");
        return item;
    }

    public PodcastItem selectItemForDownloading(int index) {
        PodcastItem item = clickOnPodcastItem(index);
        clickOnText("Загрузить");
        return item;
    }

    public void makeSamplePodcastWithUrl(String url) {
        ListAdapter adapter = listActivity.getListAdapter();
        PodcastItem item = (PodcastItem) adapter.getItem(0);
        item.setAudioUri(url);
    }
}
