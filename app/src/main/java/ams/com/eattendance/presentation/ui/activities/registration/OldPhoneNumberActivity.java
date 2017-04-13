package ams.com.eattendance.presentation.ui.activities.registration;

import ams.com.eattendance.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

public class OldPhoneNumberActivity extends AppCompatActivity {

	private Toolbar toolbar;
	private Button buttonNewRegistration;
	private Button buttonRegistrationWithOldNumber;
	private RelativeLayout relativeLayoutOldNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_old_phone_number);
		initializeActivity();
	}

	private void initializeActivity() {
		initComponents();
		initComponentValues();
		addActionListeners();

	}

	public void initComponents() {
		relativeLayoutOldNumber = (RelativeLayout) findViewById(R.id.content_old_phone_number);
		toolbar = (Toolbar) findViewById(R.id.toolbar_old_mobile_number);

		buttonNewRegistration = (Button) findViewById(R.id.button_old_mobile_next);
		buttonRegistrationWithOldNumber = (Button) findViewById(R.id.button_old_new_register);
	}

	public void initComponentValues() {

		relativeLayoutOldNumber.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		relativeLayoutOldNumber.setFocusableInTouchMode(true);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	public void addActionListeners() {

		buttonNewRegistration.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//TODO Implement it
			}
		});

		buttonRegistrationWithOldNumber.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//TODO Implement it
				Intent intent = new Intent(getApplicationContext(), AttendanceConfigurationActivity.class);
				startActivity(intent);

			}
		});

	}

}
