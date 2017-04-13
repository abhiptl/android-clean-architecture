package ams.com.eattendance.presentation.receivers;

import ams.com.eattendance.presentation.services.LocationService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LocationAlarmReceiver extends BroadcastReceiver {

	private static final String TAG = LocationService.class.getSimpleName();

	public LocationAlarmReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		context.startService(new Intent(context, LocationService.class));
	}
}
