package ams.com.eattendance.presentation.services;

import ams.com.eattendance.util.NotificationUtil;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import static android.app.AlarmManager.ELAPSED_REALTIME;

public class LoopingServiceSnipped extends Service {

	public static final long STANDARD_FREQUENCY = 10000;
	private static final String TAG = LoopingServiceSnipped.class.getSimpleName();
	private static final String INTENT_EXTRA_STOP_SERVICE = LoopingServiceSnipped.class.getSimpleName() + "_INTENT_EXTRA_STOP_SERVICE";
	private static final String INTENT_EXTRA_FREQUENCY = LoopingServiceSnipped.class.getSimpleName() + "_INTENT_EXTRA_FREQUENCY";
	private static long loopingFrequency = STANDARD_FREQUENCY;
	protected LocationManager locationManager;
	// flag for GPS Status
	boolean isGPSProviderEnabled = false;
	// flag for network status
	boolean isNetworkProviderEnabled = false;
	// flag for passive provider
	boolean isPassiveProviderEnable = false;
	boolean canGetLocation = false;
	Location location;
	double latitude;
	double longitude;

	//EXTRAS
	private Handler serviceHandler;
	private Runnable loop;

	/**
	 * Determines if the service is running (reliably) without starting it.
	 *
	 * @param context needed for SystemServices
	 * @return true if the service is running
	 */
	public static boolean isRunning(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (LoopingServiceSnipped.class.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	public static void stopService(Context c) {
		Intent i = new Intent(c, LoopingServiceSnipped.class);
		i.putExtra(INTENT_EXTRA_STOP_SERVICE, true);
		c.startService(i);
	}

	public static synchronized void setFrequency(Context c, long frequency) {
		Intent i = new Intent(c, LoopingServiceSnipped.class);
		i.putExtra(INTENT_EXTRA_FREQUENCY, frequency);
		c.startService(i);
	}

	public static long getLoopingFrequency() {
		return loopingFrequency;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		init();
	}

	private void init() {
		//do your initialization here
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		/*if (shouldStopService(intent)) {
			Log.i(TAG, "Going to stop service!");
			tryStopService();
			return Service.START_NOT_STICKY;
		} else {

		}*/
		return startOrContinueService(intent);
	}

	@Override
	public void onTaskRemoved(Intent rootIntent) {
		Log.i(TAG, "onTaskRemoved");
		Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());

		PendingIntent restartServicePendingIntent =
				PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
		AlarmManager alarmService = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmService.set(ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, restartServicePendingIntent);

		super.onTaskRemoved(rootIntent);
	}

	private void tryStopService() {
		stopLooping();
		stopSelf();
	}

	private void stopLooping() {
		if (serviceHandler != null) {
			serviceHandler.removeCallbacks(getLoop());
			serviceHandler = null;
		}
	}

	private int startOrContinueService(Intent intent) {
		/*if (hasNewFrequency(intent)) {
			setFrequency(intent);
			stopLooping();
		}*/
		if (!isServiceRunning()) {
			NotificationUtil.sendNotification("Starting looping", "LocationService", getApplicationContext());
			startLooping();
		} else {
			Log.i(TAG, "Going to continue with service!");
		}
		return Service.START_STICKY;
	}

	private boolean hasNewFrequency(Intent intent) {
		return intent.hasExtra(INTENT_EXTRA_FREQUENCY);
	}

	private void setFrequency(Intent intent) {
		if (intent.hasExtra(INTENT_EXTRA_FREQUENCY)) {
			loopingFrequency = intent.getLongExtra(INTENT_EXTRA_FREQUENCY, STANDARD_FREQUENCY);
		}
	}

	private boolean isServiceRunning() {
		return serviceHandler != null;
	}

	private boolean shouldStopService(Intent i) {
		if (i.hasExtra(INTENT_EXTRA_STOP_SERVICE)) {
			return i.getBooleanExtra(INTENT_EXTRA_STOP_SERVICE, false);
		} else {
			return false;
		}
	}

	private void startLooping() {
		if (serviceHandler == null) {
			serviceHandler = new Handler();
		}
		//serviceHandler.postDelayed(getAppDetectionLoop(), getFrequency());
		serviceHandler.postDelayed(getLoop(), getLoopingFrequency());
	}

	private Runnable getLoop() {
		if (loop == null) {
			loop = new Runnable() {

				@Override
				public void run() {
					//do your looping work here
					//startService(new Intent(getApplicationContext(), LocationService.class));
					getLocation();
					startLooping();
				}
			};
		}
		return loop;
	}

	public Location getLocation() {
		try {
			locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

			// getting GPS status
			isGPSProviderEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkProviderEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			// getting passive provider status
			isPassiveProviderEnable = locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);

			if (!isGPSProviderEnabled && !isNetworkProviderEnabled && !isPassiveProviderEnable) {
				NotificationUtil.sendNotification("All three providers disabled", "LocationService", getApplicationContext());
			} else {
				this.canGetLocation = true;
				Location networkLocation = null;
				Location gpsLocation = null;
				Location passiveLocation = null;

				Location sanelibLocation = new Location("Sanelib");
				sanelibLocation.setLatitude(23.0626324);
				sanelibLocation.setLongitude(72.5305478);

				float distanceForNetwork = 0.0f;
				float distanceForGps = 0.0f;
				float distanceForPassive = 0.0f;

				if (locationManager != null) {
					if (isNetworkProviderEnabled) {
						networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					}

					if (isGPSProviderEnabled) {
						gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					}

					if (isPassiveProviderEnable) {
						passiveLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
					}

				}

				StringBuilder locationsString = new StringBuilder();
				if (networkLocation != null) {
					distanceForNetwork = sanelibLocation.distanceTo(networkLocation);
					locationsString.append("Network").append("(").append(networkLocation.getLatitude()).append(",")
							.append(networkLocation.getLongitude()).append(",").append(distanceForNetwork).append(")").
							append(System.getProperty("line.separator"));

				}

				if (gpsLocation != null) {
					distanceForGps = sanelibLocation.distanceTo(gpsLocation);
					locationsString.append("Gps").append("(").append(gpsLocation.getLatitude()).append(",")
							.append(gpsLocation.getLongitude()).append(")").append(",").append(distanceForGps)
							.append(System.getProperty("line.separator"));
					;

				}

				if (passiveLocation != null) {
					distanceForPassive = sanelibLocation.distanceTo(passiveLocation);
					locationsString.append("Passive").append("(").append(passiveLocation.getLatitude()).append(",")
							.append(passiveLocation.getLongitude()).append(",").append(distanceForPassive).append(")");

				}

				NotificationUtil.sendNotification(locationsString.toString(), "Final LocationService", getApplicationContext());

			}
		} catch (Exception e) {
			NotificationUtil.sendNotification("Exception occured while getting location", "LocationService", getApplicationContext());
		}

		return location;
	}

}