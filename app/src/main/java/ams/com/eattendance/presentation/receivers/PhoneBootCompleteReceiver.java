package ams.com.eattendance.presentation.receivers;

import ams.com.eattendance.presentation.services.InOutDetectionService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class PhoneBootCompleteReceiver extends BroadcastReceiver {

	public PhoneBootCompleteReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		//Intent alarmIntent = new Intent(context, LocationAlarmReceiver.class);
		//AlarmUtil.addAlarm(context, alarmIntent, 1);
		context.startService(new Intent(context, InOutDetectionService.class));
		Toast.makeText(context, "Location started in background(Boot complete)", Toast.LENGTH_LONG).show();

	}
}
