package ams.com.eattendance.presentation.ui.activities.bluetooth;

import java.util.ArrayList;

import ams.com.eattendance.R;
import ams.com.eattendance.presentation.ui.activities.wifi.DividerItemDecoration;
import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class BluetoothConnectionActivity extends AppCompatActivity {

	private static final String TAG = BluetoothConnectionActivity.class.getSimpleName();
	private static final int BLUETOOTH_ENABLE_REQUEST_CODE = 1000;
	private static final int BLUETOOTH_PERMISSION_REQUEST_CODE = 2000;
	private Toolbar toolbarBluetoothConnections;
	private FloatingActionButton fabRefreshBluetoothConnection;
	private RecyclerView recyclerViewBluetoothConnections;
	private BluetoothAdapter bluetoothAdapter = null;
	private CoordinatorLayout coordinatorLayoutBluetoothConnections;
	private BluetoothConnectionAdapter bluetoothConnectionAdapter = null;
	private ArrayList<BluetoothDevice> bluetoothConnectionInfoList = new ArrayList<>();
	private final BroadcastReceiver bluetoothBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
				bluetoothConnectionInfoList = new ArrayList<BluetoothDevice>();
			} else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Discovery found new discoverable device
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				bluetoothConnectionInfoList.add(device);
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				bluetoothConnectionAdapter = new BluetoothConnectionAdapter(getApplicationContext(), bluetoothConnectionInfoList);
				recyclerViewBluetoothConnections.setAdapter(bluetoothConnectionAdapter);

			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth_connection);
		initializeActivity();

		if (bluetoothAdapter == null) {
			Snackbar unsupportedBluetoothSnackBar =
					Snackbar.make(coordinatorLayoutBluetoothConnections, "Bluetooth is unsupported by this device", Snackbar.LENGTH_LONG);
			unsupportedBluetoothSnackBar.show();
		} else {
			if (!bluetoothAdapter.isEnabled()) {
				Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(intent, BLUETOOTH_ENABLE_REQUEST_CODE);
			} else {
				if (checkBluetoothPermission() && checkLocationPermission()) {
					bluetoothAdapter.startDiscovery();
				} else {
					askPermission();
				}
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check which request we're responding to
		if (requestCode == BLUETOOTH_ENABLE_REQUEST_CODE) {
			// Make sure the request was successful
			if (resultCode == RESULT_OK) {
				if (checkBluetoothPermission() && checkLocationPermission()) {
					bluetoothAdapter.startDiscovery();
				} else {
					askPermission();
				}

			} else {
				Snackbar unsupportedBluetoothSnackBar =
						Snackbar.make(coordinatorLayoutBluetoothConnections, "Please allow app to enable Bluetooth.", Snackbar.LENGTH_LONG);
				unsupportedBluetoothSnackBar.show();
			}
		}
	}

	private void initializeActivity() {
		initComponents();
		initComponentValues();
		addActionListeners();

	}

	private void initComponents() {
		toolbarBluetoothConnections = (Toolbar) findViewById(R.id.toolbar_bluetooth_connection);
		setSupportActionBar(toolbarBluetoothConnections);

		fabRefreshBluetoothConnection = (FloatingActionButton) findViewById(R.id.fab_refresh_bluetooth);
		recyclerViewBluetoothConnections = (RecyclerView) findViewById(R.id.recyclyer_bluetooth_connections);

		coordinatorLayoutBluetoothConnections = (CoordinatorLayout) findViewById(R.id.coordinator_layout_bluetooth_connections);

		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

	}

	private void initComponentValues() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
		recyclerViewBluetoothConnections.setLayoutManager(linearLayoutManager);
		recyclerViewBluetoothConnections.setItemAnimator(new DefaultItemAnimator());
		recyclerViewBluetoothConnections.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

	}

	private void addActionListeners() {
		fabRefreshBluetoothConnection.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				refreshBluetoothFloatingAction();
			}
		});

		registerBluetoothScanAvailableReceiver();
	}

	private void refreshBluetoothFloatingAction() {
		bluetoothAdapter.startDiscovery();
	}

	private boolean checkBluetoothPermission() {
		boolean bluetoothAdminPermission =
				ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED;

		boolean bluetoothPermission =
				ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED;

		boolean bluetoothOverallPermission = (bluetoothAdminPermission == bluetoothPermission);
		return bluetoothOverallPermission;

	}

	private boolean checkLocationPermission() {
		return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);

	}

	private void askPermission() {
		ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH,
				Manifest.permission.ACCESS_COARSE_LOCATION}, BLUETOOTH_PERMISSION_REQUEST_CODE);

	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case BLUETOOTH_PERMISSION_REQUEST_CODE: {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
						&& grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
					bluetoothAdapter.startDiscovery();
				} else {
					Snackbar unsupportedBluetoothSnackBar =
							Snackbar.make(coordinatorLayoutBluetoothConnections, "Please allow app to access Bluetooth.",
									Snackbar.LENGTH_LONG);
					unsupportedBluetoothSnackBar.show();
				}
				break;
			}
		}
	}

	private void registerBluetoothScanAvailableReceiver() {

		// Register for broadcasts when a device is discovered.
		IntentFilter bluetoothDeviceDiscoveryIntent = new IntentFilter();

		bluetoothDeviceDiscoveryIntent.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		bluetoothDeviceDiscoveryIntent.addAction(BluetoothDevice.ACTION_FOUND);
		bluetoothDeviceDiscoveryIntent.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

		registerReceiver(bluetoothBroadcastReceiver, bluetoothDeviceDiscoveryIntent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(bluetoothBroadcastReceiver);
	}

	@Override
	public void onPause() {
		if (bluetoothAdapter != null) {
			if (bluetoothAdapter.isDiscovering()) {
				bluetoothAdapter.cancelDiscovery();
			}
		}

		super.onPause();
	}

}
