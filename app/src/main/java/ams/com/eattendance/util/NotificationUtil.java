package ams.com.eattendance.util;

import ams.com.eattendance.R;
import ams.com.eattendance.presentation.ui.activities.WelcomeActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Abhishek on 3/9/2017.
 */
public class NotificationUtil {

	private static final String TAG = NotificationUtil.class.getSimpleName();

	public static int GEOFENCE_NOTIFICATION_ID = 0;

	public static void sendNotification(final String msg, final String title, final Context applicationContext) {

		NotificationManager notificationManager = (NotificationManager) applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent notificationIntent = new Intent(applicationContext, WelcomeActivity.class);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(applicationContext);
		stackBuilder.addParentStack(WelcomeActivity.class);
		stackBuilder.addNextIntent(notificationIntent);
		PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

		notificationManager.notify(GEOFENCE_NOTIFICATION_ID, createNotification(msg, title, notificationPendingIntent, applicationContext));
		GEOFENCE_NOTIFICATION_ID++;

	}

	// Create notification
	private static Notification createNotification(String msg, String title, PendingIntent notificationPendingIntent, Context context) {
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
		notificationBuilder.setSmallIcon(R.mipmap.ic_launcher).setColor(Color.RED).setContentTitle(title)
				.setContentText("Pull down for more information").setContentIntent(notificationPendingIntent)
				.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND).setAutoCancel(true)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg));
		return notificationBuilder.build();
	}

}
