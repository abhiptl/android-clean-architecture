package ams.com.eattendance.presentation.services;

import ams.com.eattendance.util.NotificationUtil;
import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;

public class LocationService extends IntentService {

	private static final String TAG = LocationService.class.getSimpleName();
	private static final int TWO_MINUTES = 1000 * 60 * 2;
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

	public LocationService() {
		super("LocationService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		getLocation();
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
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

	/**
	 * Determines whether one Location reading is better than the current Location fix
	 *
	 * @param location            The new Location that you want to evaluate
	 * @param currentBestLocation The current Location fix, to which you want to compare the new one
	 */
	protected boolean isBetterLocation(Location location, Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
			return true;
		}
		return false;
	}

	/**
	 * Checks whether two providers are the same
	 */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

}
