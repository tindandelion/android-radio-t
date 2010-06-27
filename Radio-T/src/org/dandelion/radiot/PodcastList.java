package org.dandelion.radiot;

import java.util.List;

public class PodcastList {
	public interface IPodcastListModel {
		public List<PodcastItem> retrievePodcasts() throws Exception; 
	}
	
	public interface IPodcastListView {
		
	}
	
	public interface IPodcastListPresenter { 
		
	}
}
