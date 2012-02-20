package org.dandelion.radiot.live.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;

@SuppressWarnings("rawtypes")
abstract class Foregrounder {
	private static final Class[] signature = new Class[] { int.class,
			Notification.class };

	public abstract void stopForeground();

	public abstract void startForeground(int id, Notification notification);

	public static Foregrounder create(Service service) {
		return isNewApi() ? foregrounder20(service) : foregrounder15(service);
	}

	private static boolean isNewApi() {
		try {
			Service.class.getMethod("startForeground", signature);
			return true;
		} catch (NoSuchMethodException e) {
			return false;
		}
	}

	private static Foregrounder foregrounder20(final Service service) {
		return new Foregrounder() {
			@Override
			public void stopForeground() {
				service.stopForeground(true);
			}

			@Override
			public void startForeground(int id, Notification notification) {
				service.startForeground(id, notification);
			}
		};
	}

	private static Foregrounder foregrounder15(final Service service) {
		final NotificationManager nm = (NotificationManager) service
				.getSystemService(Context.NOTIFICATION_SERVICE);
		return new Foregrounder() {
			private int notificationId;

			public void stopForeground() {
				nm.cancel(notificationId);
				service.setForeground(false);
			}

			@Override
			public void startForeground(int id, Notification notification) {
				service.setForeground(true);
				nm.notify(id, notification);
				this.notificationId = id;
			}
		};
	}
}
