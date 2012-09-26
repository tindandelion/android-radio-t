package org.dandelion.radiot.accepttest.drivers;

import android.app.Instrumentation;
import android.app.ListActivity;
import android.widget.ListAdapter;
import com.jayway.android.robotium.solo.Solo;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.ui.PodcastVisual;

public class PodcastListUiDriver extends Solo {
    private ListActivity listActivity;

    public PodcastListUiDriver(Instrumentation instrumentation, ListActivity activity) {
        super(instrumentation, activity);
        this.listActivity = activity;
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
        ListAdapter adapter = listActivity.getListAdapter();
        PodcastVisual pv = (PodcastVisual) adapter.getItem(index);
        clickInList(index);
        return pv.podcast;
    }

    public void makeSamplePodcastWithUrl(String title, String url) {
        ListAdapter adapter = listActivity.getListAdapter();
        PodcastItem item = ((PodcastVisual) adapter.getItem(0)).podcast;
        item.setTitle(title);
        item.setAudioUri(url);
    }
}
