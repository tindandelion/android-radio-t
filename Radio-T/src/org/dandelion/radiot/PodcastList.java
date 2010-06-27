package org.dandelion.radiot;

import java.util.List;

public class PodcastList {
	public interface IPodcastListModel {
		List<PodcastItem> retrievePodcasts() throws Exception; 
	}
	
	public interface IPodcastListView {
		void updatePodcasts(List<PodcastItem> podcasts);
	}
	
	public interface IPodcastListPresenter { 
		void initialize(IPodcastListModel model);
	}
}
