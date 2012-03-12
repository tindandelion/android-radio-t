package org.dandelion.radiot.helpers;


import org.dandelion.radiot.podcasts.ui.PodcastListActivity;
import org.dandelion.radiot.home_screen.HomeScreenActivity;

import android.app.Activity;
import android.app.Instrumentation;

import com.jayway.android.robotium.solo.Solo;

public class ApplicationDriver extends Solo {
	public ApplicationDriver(Instrumentation inst, Activity activity) {
		super(inst, activity);
	}

	public void assertOnHomeScreen() {
		assertCurrentActivity("Must be on the home screen", HomeScreenActivity.class);
	}
	
	public PodcastListActivity visitMainShowPage() {
		clickOnText("Подкасты");
		assertCurrentActivity("Must be on the main show page", PodcastListActivity.class);
		return (PodcastListActivity) getCurrentActivity();
	}

	public void visitAfterShowPage() {
		clickOnText("После-шоу");
		assertCurrentActivity("Must be on the after show page", PodcastListActivity.class);
	}

	public void waitSomeTime() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void assertShowingPodcastList() {
		assertCurrentActivity("Must show podcast list", PodcastListActivity.class);
	}
	

}