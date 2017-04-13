package ams.com.eattendance.presentation.ui.activities;

import ams.com.eattendance.R;
import ams.com.eattendance.presentation.services.InOutDetectionService;
import ams.com.eattendance.presentation.ui.activities.bluetooth.BluetoothConnectionActivity;
import ams.com.eattendance.presentation.ui.activities.geolocation.MyGeoLocationActivity;
import ams.com.eattendance.presentation.ui.activities.wifi.WifiConnectionActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

		setSupportActionBar(toolbar);

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle =
				new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		/*Intent alarmIntent = new Intent(this, LocationAlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_NO_CREATE);

		if(pendingIntent == null) {
			NotificationUtil.sendNotification("Pending intent not exist", "Welcome Activity", this);
			AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			long interval = 120000;
			manager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime()+2000,interval,  pendingIntent);
			Toast.makeText(this, "Location started in background(Main)", Toast.LENGTH_LONG).show();

		} else {
			NotificationUtil.sendNotification("Pending intent exist", "Welcome Activity", this);
		}*/

	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.action_bar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_notifications) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.

		Fragment fragment = null;

		int id = item.getItemId();
		String title = null;

		if (id == R.id.nav_dashboard) {

		} else if (id == R.id.nav_attendance) {
			//Intent alarmIntent = new Intent(WelcomeActivity.this, LocationAlarmReceiver.class);
			//AlarmUtil.addAlarm(this, alarmIntent, 1);

			startService(new Intent(this, InOutDetectionService.class));
			Toast.makeText(this, "Location started in background(Main)", Toast.LENGTH_LONG).show();
		} else if (id == R.id.nav_setting) {

		} else if (id == R.id.nav_profile) {

		} else if (id == R.id.nav_geolocation) {
			startActivity(new Intent(this, MyGeoLocationActivity.class));
		} else if (id == R.id.nav_wifi) {
			startActivity(new Intent(this, WifiConnectionActivity.class));
		} else if (id == R.id.nav_bluetooth) {
			startActivity(new Intent(this, BluetoothConnectionActivity.class));
		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}
}
