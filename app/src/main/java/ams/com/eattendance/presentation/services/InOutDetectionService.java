package ams.com.eattendance.presentation.services;

import ams.com.eattendance.util.NotificationUtil;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import static android.app.AlarmManager.ELAPSED_REALTIME;

public class InOutDetectionService extends Service implements LocationListener {

	private static final String TAG = InOutDetectionService.class.getSimpleName();
	protected LocationManager locationManager;
	// flag for GPS Status
	boolean isGPSProviderEnabled = false;
	// flag for network status
	boolean isNetworkProviderEnabled = false;
	// flag for passive provider
	boolean isPassiveProviderEnable = false;
	private Handler serviceHandler;
	private Runnable serviceLoop;
	private long loopingFrequency = 20000;

	public InOutDetectionService() {
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		initService();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, getLoopingFrequency(), 20f, this);
		startDetection();
		return Service.START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onTaskRemoved(Intent rootIntent) {
		Log.i(TAG, "onTaskRemoved");
		Intent restartInOutServiceIntent = new Intent(getApplicationContext(), this.getClass());

		PendingIntent restartInOutServicePendingIntent =
				PendingIntent.getService(getApplicationContext(), 1, restartInOutServiceIntent, PendingIntent.FLAG_ONE_SHOT);
		AlarmManager alarmService = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmService.set(ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, restartInOutServicePendingIntent);

		super.onTaskRemoved(rootIntent);
	}

	/**
	 * Do initialization of service
	 */
	private void initService() {
	}

	private void startLooping() {
		if (serviceHandler == null) {
			serviceHandler = new Handler();
		}
		serviceHandler.postDelayed(getLoop(), getLoopingFrequency());
	}

	private void stopLooping() {
		if (serviceHandler != null) {
			serviceHandler.removeCallbacks(getLoop());
			serviceHandler = null;
		}
	}

	private Runnable getLoop() {
		if (serviceLoop == null) {
			serviceLoop = new Runnable() {

				@Override
				public void run() {
					// Detection logic comes here
					getLocation();
					startLooping();
				}
			};
		}
		return serviceLoop;
	}

	private void startDetection() {
		startLooping();
	}

	public Location getLocation() {
		try {

			// getting GPS status
			isGPSProviderEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkProviderEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			// getting passive provider status
			isPassiveProviderEnable = locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);

			if (!isGPSProviderEnabled && !isNetworkProviderEnabled && !isPassiveProviderEnable) {
				NotificationUtil.sendNotification("All three providers disabled", "LocationManager", getApplicationContext());
			} else {
				Location networkLocation = null;
				Location gpsLocation = null;
				Location passiveLocation = null;

				Location sanelibLocation = new Location("Sanelib");
				sanelibLocation.setLatitude(23.0626324);
				sanelibLocation.setLongitude(72.5305478);

				float distanceForNetwork = 0.0f;
				float distanceForGps = 0.0f;
				float distanceForPassive = 0.0f;
				float distanceForGoogleAPI = 0.0f;

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
							.append(networkLocation.getLongitude()).append(",").append(networkLocation.getAccuracy()).append(")").
							append(System.getProperty("line.separator"));

				}

				if (gpsLocation != null) {
					distanceForGps = sanelibLocation.distanceTo(gpsLocation);
					locationsString.append("Gps").append("(").append(gpsLocation.getLatitude()).append(",")
							.append(gpsLocation.getLongitude()).append(")").append(",").append(gpsLocation.getAccuracy())
							.append(System.getProperty("line.separator"));
					;

				}

				if (passiveLocation != null) {
					distanceForPassive = sanelibLocation.distanceTo(passiveLocation);
					locationsString.append("Passive").append("(").append(passiveLocation.getLatitude()).append(",")
							.append(passiveLocation.getLongitude()).append(",").append(passiveLocation.getAccuracy())
							.append(System.getProperty("line.separator"));

				}

				if (locationsString.length() == 0) {
					locationsString.append("No location detected from any provider");
				}

				NotificationUtil.sendNotification(locationsString.toString(), "InOutDetectionService", getApplicationContext());

			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			NotificationUtil.sendNotification("Exception :" + e.getMessage(), "LocationManager", getApplicationContext());
		}

		return null;
	}

	public long getLoopingFrequency() {
		return loopingFrequency;
	}

	@Override
	public void onLocationChanged(Location location) {

		StringBuilder locationsString = new StringBuilder();
		locationsString.append("(");
		if (location != null) {
			if (location != null) {
				locationsString.append(location.getLatitude()).append(",").append(location.getLongitude()).append(",")
						.append(location.getAccuracy());
			}
		}

		locationsString.append(")");
		NotificationUtil.sendNotification(locationsString.toString(), locationsString.toString(), getApplicationContext());
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}
}
