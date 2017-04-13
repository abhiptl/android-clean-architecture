package ams.com.eattendance.presentation.ui.activities.wifi;

import java.util.ArrayList;
import java.util.List;

import ams.com.eattendance.R;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class WifiConnectionActivity extends AppCompatActivity {

	private static final String TAG = WifiConnectionActivity.class.getSimpleName();
	private static final int WIFI_ACCESS_FINE_LOCATION_REQUEST_CODE = 111;
	public WifiManager wifiManager;
	public WifiReceiver wifiReceiver;
	private Toolbar toolbar;
	private FloatingActionButton fabRefreshWifiConnection;
	private RecyclerView recyclerViewWifiConnections;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi_connection);
		initializeActivity();
	}

	private void initializeActivity() {
		initComponents();
		initComponentValues();
		addActionListeners();

	}

	private void initComponents() {
		toolbar = (Toolbar) findViewById(R.id.toolbar_wifi_connection);
		setSupportActionBar(toolbar);

		fabRefreshWifiConnection = (FloatingActionButton) findViewById(R.id.fab_refresh_wifi);
		recyclerViewWifiConnections = (RecyclerView) findViewById(R.id.recyclyer_wifi_connections);

		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
	}

	private void initComponentValues() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
		recyclerViewWifiConnections.setLayoutManager(linearLayoutManager);
		recyclerViewWifiConnections.setItemAnimator(new DefaultItemAnimator());
		recyclerViewWifiConnections.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

	}

	private void addActionListeners() {
		fabRefreshWifiConnection.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				refreshFloatingAction();
			}
		});

		registerWifiScanAvailableReceiver();

	}

	private void refreshFloatingAction() {
		if (checkPermission()) {
			wifiManager.startScan();
		} else {
			askPermission();
		}

	}

	private void registerWifiScanAvailableReceiver() {
		wifiManager.setWifiEnabled(true);
		wifiReceiver = new WifiReceiver();

		registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

		// https://developer.android.com/about/versions/marshmallow/android-6.0-changes.html#behavior-hardware-id

		if (checkPermission()) {
			wifiManager.startScan();
		} else {
			askPermission();
		}

	}

	private boolean checkPermission() {
		return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
	}

	private void askPermission() {
		ActivityCompat
				.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, WIFI_ACCESS_FINE_LOCATION_REQUEST_CODE);

	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case WIFI_ACCESS_FINE_LOCATION_REQUEST_CODE: {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					wifiManager.startScan();
				} else {
					// TODO : need to think
				}
				break;
			}
		}
	}

	class WifiReceiver extends BroadcastReceiver {

		public void onReceive(Context c, Intent intent) {
			List<ScanResult> mScanResults = wifiManager.getScanResults();
			ArrayList<WifiConnectionInfo> wifiConnectionInfoList = new ArrayList<>();
			WifiConnectionInfo wifiConnectionInfo = null;

			WifiInfo connectionInfo = wifiManager.getConnectionInfo();
			String connectedWifiSsid = null;
			if (connectionInfo != null) {
				connectedWifiSsid = connectionInfo.getBSSID();
			}

			for (int i = 0; i < mScanResults.size(); i++) {
				ScanResult scanResult = mScanResults.get(i);

				Boolean isConnected = scanResult.BSSID.equals(connectedWifiSsid);

				wifiConnectionInfo = new WifiConnectionInfo(scanResult.SSID, scanResult.BSSID, isConnected);
				wifiConnectionInfoList.add(wifiConnectionInfo);
			}

			WifiConnectionAdapter wificonnectionAdapter = new WifiConnectionAdapter(getApplicationContext(), wifiConnectionInfoList);
			recyclerViewWifiConnections.setAdapter(wificonnectionAdapter);
			wificonnectionAdapter.notifyDataSetChanged();

		}
	}

}



