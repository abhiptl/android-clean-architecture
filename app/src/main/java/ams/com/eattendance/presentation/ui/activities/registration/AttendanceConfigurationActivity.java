package ams.com.eattendance.presentation.ui.activities.registration;

import java.util.ArrayList;
import java.util.Calendar;

import ams.com.eattendance.R;
import ams.com.eattendance.presentation.ui.activities.WelcomeActivity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

public class AttendanceConfigurationActivity extends AppCompatActivity implements EditText.OnClickListener {

	private Toolbar toolbar;
	private Spinner spinnerLocations;
	private Spinner spinnerDepartments;
	private RelativeLayout relativeLayout;

	private Spinner spinnerWorkingMon;
	private Spinner spinnerWorkingTue;
	private Spinner spinnerWorkingWed;
	private Spinner spinnerWorkingThu;
	private Spinner spinnerWorkingFri;
	private Spinner spinnerWorkingSat;
	private Spinner spinnerWorkingSun;

	private EditText editTextMonStartTime;
	private EditText editTextMonHours;

	private EditText editTextTueStartTime;
	private EditText editTextTueHours;

	private EditText editTextWedStartTime;
	private EditText editTextWedHours;

	private EditText editTextThuStartTime;
	private EditText editTextThuHours;

	private EditText editTextFriStartTime;
	private EditText editTextFriHours;

	private EditText editTextSatStartTime;
	private EditText editTextSatHours;

	private EditText editTextSunStartTime;
	private EditText editTextSunHours;

	private Context context;

	private Button buttonFinish;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attendance_configuration);
		initializeActivity();
	}

	private void initializeActivity() {
		initComponents();
		initComponentValues();
		addActionListeners();

	}

	private void initComponents() {
		toolbar = (Toolbar) findViewById(R.id.toolbar_attendance_configuration);

		spinnerLocations = (Spinner) findViewById(R.id.spinner_location);
		spinnerDepartments = (Spinner) findViewById(R.id.spinner_department);

		relativeLayout = (RelativeLayout) findViewById(R.id.content_attendance_configuration);

		spinnerWorkingMon = (Spinner) findViewById(R.id.spinner_working_mon);
		spinnerWorkingTue = (Spinner) findViewById(R.id.spinner_working_tue);
		spinnerWorkingWed = (Spinner) findViewById(R.id.spinner_working_wed);
		spinnerWorkingThu = (Spinner) findViewById(R.id.spinner_working_thu);
		spinnerWorkingFri = (Spinner) findViewById(R.id.spinner_working_fri);
		spinnerWorkingSat = (Spinner) findViewById(R.id.spinner_working_sat);
		spinnerWorkingSun = (Spinner) findViewById(R.id.spinner_working_sun);

		context = this.getApplicationContext();

		editTextMonStartTime = (EditText) findViewById(R.id.edittext_mon_starttime);
		editTextMonHours = (EditText) findViewById(R.id.edittext_mon_hours);

		editTextTueStartTime = (EditText) findViewById(R.id.edittext_tue_starttime);
		editTextTueHours = (EditText) findViewById(R.id.edittext_tue_hours);

		editTextWedStartTime = (EditText) findViewById(R.id.edittext_wed_starttime);
		editTextWedHours = (EditText) findViewById(R.id.edittext_wed_hours);

		editTextThuStartTime = (EditText) findViewById(R.id.edittext_thu_starttime);
		editTextThuHours = (EditText) findViewById(R.id.edittext_thu_hours);

		editTextFriStartTime = (EditText) findViewById(R.id.edittext_fri_starttime);
		editTextFriHours = (EditText) findViewById(R.id.edittext_fri_hours);

		editTextSatStartTime = (EditText) findViewById(R.id.edittext_sat_starttime);
		editTextSatHours = (EditText) findViewById(R.id.edittext_sat_hours);

		editTextSunStartTime = (EditText) findViewById(R.id.edittext_sun_starttime);
		editTextSunHours = (EditText) findViewById(R.id.edittext_sun_hours);

		buttonFinish = (Button) findViewById(R.id.button_finish);

	}

	private void initComponentValues() {
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		relativeLayout.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		relativeLayout.setFocusableInTouchMode(true);

		fillLocationsSpinner();
		fillDepartmentsSpinner();
		fillWorkingSpinners();
	}

	private void addActionListeners() {
		editTextMonStartTime.setOnClickListener(this);
		editTextMonHours.setOnClickListener(this);

		editTextTueStartTime.setOnClickListener(this);
		editTextTueHours.setOnClickListener(this);

		editTextWedStartTime.setOnClickListener(this);
		editTextWedHours.setOnClickListener(this);

		editTextThuStartTime.setOnClickListener(this);
		editTextThuHours.setOnClickListener(this);

		editTextFriStartTime.setOnClickListener(this);
		editTextFriHours.setOnClickListener(this);

		editTextSatStartTime.setOnClickListener(this);
		editTextSatHours.setOnClickListener(this);

		editTextSunStartTime.setOnClickListener(this);
		editTextSunHours.setOnClickListener(this);

		buttonFinish.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finishAction();
			}
		});

	}

	private void fillLocationsSpinner() {
		ArrayList<String> listLocation = new ArrayList<>();
		listLocation.add(getResources().getString(R.string.activity_attendance_configuration_employee_select_location));
		listLocation.add("Sataddhar");
		listLocation.add("Naranpura");
		listLocation.add("Viratnagar");

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listLocation);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerLocations.setAdapter(dataAdapter);

	}

	private void fillDepartmentsSpinner() {
		ArrayList<String> listDepartment = new ArrayList<>();
		listDepartment.add(getResources().getString(R.string.activity_attendance_configuration_employee_select_department));
		listDepartment.add("HR");
		listDepartment.add("Admin");
		listDepartment.add("Security");

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listDepartment);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerDepartments.setAdapter(dataAdapter);

	}

	private void fillWorkingSpinners() {
		ArrayList<String> listWorkings = new ArrayList<>();
		listWorkings.add("Working");
		listWorkings.add("Off");
		listWorkings.add("1-3-5 Off");
		listWorkings.add("2-4 Off");

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listWorkings);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinnerWorkingMon.setAdapter(dataAdapter);
		spinnerWorkingTue.setAdapter(dataAdapter);
		spinnerWorkingWed.setAdapter(dataAdapter);
		spinnerWorkingThu.setAdapter(dataAdapter);
		spinnerWorkingFri.setAdapter(dataAdapter);
		spinnerWorkingSat.setAdapter(dataAdapter);
		spinnerWorkingSun.setAdapter(dataAdapter);
	}

	@Override
	public void onClick(View v) {
		int currentHour, currentMinute;

		final Calendar c = Calendar.getInstance();
		currentHour = c.get(Calendar.HOUR_OF_DAY);
		currentMinute = c.get(Calendar.MINUTE);

		final EditText editView = (EditText) v;
		// Launch Time Picker Dialog
		TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

				editView.setText(hourOfDay + " : " + minute);
			}
		}, currentHour, currentMinute, true);
		timePickerDialog.show();
	}

	private void finishAction() {
		//TODO : Complex implementation to setup Application enviornment
		Intent dashboardIntent = new Intent(this, WelcomeActivity.class);
		startActivity(dashboardIntent);
	}
}
