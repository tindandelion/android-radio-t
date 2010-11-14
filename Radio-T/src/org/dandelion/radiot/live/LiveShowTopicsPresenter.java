package org.dandelion.radiot.live;

import java.util.ArrayList;
import java.util.List;

public class LiveShowTopicsPresenter {
	private LiveShowTopics view;

	public LiveShowTopicsPresenter(LiveShowTopics view) {
		this.view = view;
	}

	public void refreshTopics() {
		view.setTopics(getTopics());
	}

	private List<ShowTopic> getTopics() {
		ArrayList<ShowTopic> list = new ArrayList<ShowTopic>();
		list.add(new ShowTopic("Topic 1"));
		list.add(new ShowTopic("Topic 2"));
		list.add(new ShowTopic("Topic 3"));
		return list;
	}

	public void cancelAll() {
	}
}
