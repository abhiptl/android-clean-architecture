package ams.com.eattendance.presentation.ui.activities.geolocation;

import ams.com.eattendance.AMSApplication;
import ams.com.eattendance.R;
import ams.com.eattendance.data.database.impl.DatabaseRepositoryImpl;
import ams.com.eattendance.data.database.model.DaoSession;
import ams.com.eattendance.domain.executor.impl.ThreadExecutor;
import ams.com.eattendance.domain.model.Geofence;
import ams.com.eattendance.presentation.presenters.GeoFencingPresenter;
import ams.com.eattendance.presentation.presenters.impl.GeoFencingPresenterImpl;
import ams.com.eattendance.presentation.threading.MainThreadImpl;
import ams.com.eattendance.util.CommonUtils;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MyGeoLocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener, LocationListener, GeoFencingPresenter.View {

	private static final String TAG = MyGeoLocationActivity.class.getSimpleName();
	private final int REQ_PERMISSION = 999;
	private GoogleMap googleMap;
	private MapFragment mapFragment;
	private String geoLocationName = null;
	private double geoLocationLatitude;
	private double geoLocationLongitude;
	private double geoLocationRadius;
	private LocationRequest locationRequest;
	private GoogleApiClient googleApiClient;
	private Marker currentLocationMarker;
	private Marker geoFenceMarker;
	private Toolbar toolbar;
	private GeoFencingPresenter geoFencingPresenter;
	private Location lastLocation;

	private ProgressDialog progressDialog;

	private DaoSession daoSession;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_geo);
		initializeActivity();
		retrieveGeoFencing();

	}

	private void initializeActivity() {
		initComponents();
		initComponentValues();
		addActionListeners();

	}

	private void initComponents() {
		toolbar = (Toolbar) findViewById(R.id.toolbarMyGeoLocation);

		mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		daoSession = ((AMSApplication)this.getApplication()).getDaoSession();
		geoFencingPresenter =
				new GeoFencingPresenterImpl(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this, new DatabaseRepositoryImpl
						(daoSession));

	}

	private void initComponentValues() {
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private void addActionListeners() {

	}

	private void retrieveGeoFencing() {
		Log.i(TAG, "retrieveGeoFencing");
		showProgress();
		geoFencingPresenter.getGeoFencing(123);
	}

	@Override
	public void onMapReady(final GoogleMap googleMap) {
		this.googleMap = googleMap;
		buildGoogleApiClient();

		if (checkPermission()) {
			this.googleMap.setMyLocationEnabled(true);
		} else {
			askPermission();
		}

		this.googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {

			@Override
			public boolean onMyLocationButtonClick() {
				myLocationClickAction();
				return true;
			}
		});

	}

	private void myLocationClickAction() {
		if (checkPermission()) {
			Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

			if (lastLocation != null) {
				this.lastLocation = lastLocation;
				markCurrentLocationMarker(lastLocation);
			} else if (lastLocation != null) {
				markCurrentLocationMarker(lastLocation);
			}
		} else {

		}
	}

	private boolean checkPermission() {
		return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
	}

	private void askPermission() {
		ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQ_PERMISSION);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case REQ_PERMISSION: {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					buildGoogleApiClient();

				} else {

				}
				break;
			}
		}
	}

	protected synchronized void buildGoogleApiClient() {
		googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();
		googleApiClient.connect();
	}

	@Override
	public void onLocationChanged(Location location) {
		lastLocation = location;
		markCurrentLocationMarker(location);

	}

	private void markCurrentLocationMarker(final Location location) {
		if (currentLocationMarker != null) {
			currentLocationMarker.remove();
		}

		//Place current location marker
		LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(latLng);
		markerOptions.title("My Location");
		markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
		currentLocationMarker = googleMap.addMarker(markerOptions);
		currentLocationMarker.showInfoWindow();

		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		builder.include(currentLocationMarker.getPosition());
		builder.include(geoFenceMarker.getPosition());

		LatLngBounds bounds = builder.build();

		int width = getResources().getDisplayMetrics().widthPixels;
		int height = getResources().getDisplayMetrics().heightPixels;
		int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

		final CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);

		googleMap.animateCamera(cameraUpdate);

		//optionally, stop location updates if only current location is needed
		if (googleApiClient != null) {
			LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
		}
	}

	private int calculateZoomLevel(int screenWidth, int radius) {
		// http://stackoverflow.com/questions/6002563/android-how-do-i-set-the-zoom-level-of-map-view-to-1-km-radius-around-my-current
		double equatorLength = 40075004; // in meters
		double widthInPixels = screenWidth;
		double metersPerPixel = equatorLength / 256;
		double newRadius = radius + 500; // show 500 meter area around the fencing
		int zoomLevel = 1;
		while ((metersPerPixel * widthInPixels) > newRadius) {
			metersPerPixel /= 2;
			++zoomLevel;
		}
		return zoomLevel;
	}

	@Override
	public void onConnected(@Nullable Bundle bundle) {
		locationRequest = new LocationRequest();
		locationRequest.setInterval(5000);
		locationRequest.setFastestInterval(3000);
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		if (checkPermission()) {
			LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
		}
	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

	}

	@Override
	public void onStop() {
		super.onStop();

		//stop location updates when Activity is no longer active
		if (googleApiClient != null) {
			LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
		}

	}

	@Override
	public void geoFencingRetrieved(Geofence geofence) {
		Log.i(TAG, "geoFencingRetrieved:" + geofence);
		geoLocationName = geofence.getGeoLocationName();
		geoLocationLatitude = geofence.getGeoLocationLatitude();
		geoLocationLongitude = geofence.getGeoLocationLatitude();
		geoLocationRadius = geofence.getGeoLocationRadius();

		LatLng geoLocation = new LatLng(geoLocationLatitude, geoLocationLongitude);

		MarkerOptions geoLocationMarker =
				new MarkerOptions().position(geoLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
						.title(geoLocationName).draggable(true);

		geoFenceMarker = this.googleMap.addMarker(geoLocationMarker);
		geoFenceMarker.showInfoWindow();

		CircleOptions circleOptions = new CircleOptions().center(geoLocation).strokeColor(Color.argb(50, 70, 70, 70)).
				fillColor(Color.argb(100, 245, 93, 84)).radius(geoLocationRadius);

		this.googleMap.addCircle(circleOptions);

		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		builder.include(geoFenceMarker.getPosition());

		LatLngBounds bounds = builder.build();

		int width = getResources().getDisplayMetrics().widthPixels;
		int height = getResources().getDisplayMetrics().heightPixels;
		int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

		float zoom = calculateZoomLevel(width, (int) geoLocationRadius);
		final CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(geoFenceMarker.getPosition(), zoom);
		googleMap.animateCamera(cameraUpdate);

		hideProgress();
	}

	@Override
	public void geoFencingNotFound() {
		showError("Sorry ! Location data not found");
	}

	@Override
	public void showProgress() {
		hideProgress();
		progressDialog = CommonUtils.showLoadingDialog(this);
	}

	@Override
	public void hideProgress() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.cancel();
		}
	}

	@Override
	public void showError(String message) {
		hideProgress();
		CommonUtils.showSnackBar(this, message, findViewById(R.id.content_my_geo));
	}
}
