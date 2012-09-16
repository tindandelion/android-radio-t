package org.dandelion.radiot.accepttest.drivers;

import android.app.Instrumentation;
import android.app.ListActivity;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.jayway.android.robotium.solo.Solo;
import org.dandelion.radiot.podcasts.core.PodcastItem;

import java.util.ArrayList;

import static junit.framework.Assert.assertTrue;

public class PodcastListUiDriver extends Solo {
    private ListActivity listActivity;

    public PodcastListUiDriver(Instrumentation instrumentation, ListActivity activity) {
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

    public void makeSamplePodcastWithUrl(String title, String url) {
        ListAdapter adapter = listActivity.getListAdapter();
        PodcastItem item = (PodcastItem) adapter.getItem(0);
        item.setTitle(title);
        item.setAudioUri(url);
    }

    public ListView listView() {
        assertTrue("List view haven't appeared", waitForView(ListView.class));
        ArrayList<ListView> listViews = getCurrentListViews();
        return listViews.get(0);
    }
}
