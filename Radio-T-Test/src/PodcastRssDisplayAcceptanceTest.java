import org.dandelion.radiot.PodcastListActivity;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.UiThreadTest;
import android.widget.ListView;

public class PodcastRssDisplayAcceptanceTest extends
		ActivityUnitTestCase<PodcastListActivity> {

	public PodcastRssDisplayAcceptanceTest() {
		super(PodcastListActivity.class);
	}

	@UiThreadTest
	public void testDisplayTheListOfPodcastsFromRss() throws Exception {
		PodcastListActivity activity = startActivity(new Intent(), null, null);
		ListView list = activity.getListView();

		assertEquals(16, list.getCount());
	}

}
