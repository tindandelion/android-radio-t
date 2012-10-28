package org.dandelion.radiot.accepttest.drivers;

import android.app.Instrumentation;
import android.widget.ListAdapter;
import com.jayway.android.robotium.solo.Solo;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.ui.PodcastListActivity;
import org.dandelion.radiot.podcasts.ui.PodcastVisual;

public class PodcastListUiDriver extends Solo {
    private PodcastListActivity activity;

    public PodcastListUiDriver(Instrumentation instrumentation, PodcastListActivity activity) {
        super(instrumentation, activity);
        this.activity = activity;
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

    private PodcastItem clickOnPodcastItem(int index) {
        ListAdapter adapter = activity.getListAdapter();
        PodcastVisual pv = (PodcastVisual) adapter.getItem(index);
        clickInList(index);
        return pv.podcast;
    }

    public void makeSamplePodcastWithUrl(String title, String url) {
        ListAdapter adapter = activity.getListAdapter();
        PodcastItem item = ((PodcastVisual) adapter.getItem(0)).podcast;
        item.title = title;
        item.setAudioUri(url);
    }
}
