package ams.com.eattendance.presentation.ui.activities.registration;

import ams.com.eattendance.R;
import ams.com.eattendance.data.network.impl.WebServiceRepositoryImpl;
import ams.com.eattendance.domain.executor.impl.ThreadExecutor;
import ams.com.eattendance.presentation.presenters.ValidateLoginPresenter;
import ams.com.eattendance.presentation.presenters.impl.ValidateLoginPresenterImpl;
import ams.com.eattendance.presentation.threading.MainThreadImpl;
import ams.com.eattendance.util.CommonUtils;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RegistrationActivity extends AppCompatActivity implements ValidateLoginPresenter.View{

	private static final String TAG = RegistrationActivity.class.getSimpleName();

	private RelativeLayout relativeLayout;
	private TextView textAmsSite;
	private TextView textTermCondition;
	private Button buttonNext;
	private Button buttonRegisterCompany;
	private ValidateLoginPresenter validateLoginPresenter;
	private EditText editTextCompanyName;
	private EditText editTextPhoneNumber;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		setTitle(getResources().getString(R.string.activity_registration_title));
		initializeActivity();
	}

	private void initializeActivity() {
		initComponents();
		initComponentValues();
		addActionListeners();

	}

	private void initComponents() {
		relativeLayout = (RelativeLayout) findViewById(R.id.activity_registration);
		textAmsSite = (TextView) findViewById(R.id.text_ams_site);
		textTermCondition = (TextView) findViewById(R.id.text_term_condition);
		buttonNext = (Button) findViewById(R.id.button_next);
		buttonRegisterCompany = (Button) findViewById(R.id.button_register_company);
		editTextCompanyName = (EditText)  findViewById(R.id.edittext_company_name);
		editTextPhoneNumber = (EditText)  findViewById(R.id.edittext_mobile_number);

		validateLoginPresenter = new ValidateLoginPresenterImpl(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this,
				new WebServiceRepositoryImpl());
	}

	private void initComponentValues() {
		relativeLayout.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		relativeLayout.setFocusableInTouchMode(true);
	}

	private void addActionListeners() {
		textTermCondition.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				openTermsAndConditionDialogue();
			}
		});

		textAmsSite.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				openAmsSiteView();
			}
		});

		buttonNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				nextClickAction();
			}
		});

		buttonRegisterCompany.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				openAmsSiteView();
			}
		});
	}

	private void openTermsAndConditionDialogue() {
		final AlertDialog.Builder termsConditionDialogueBuilder = new AlertDialog.Builder(RegistrationActivity.this);

		termsConditionDialogueBuilder.setTitle(getResources().getString(R.string.dialogue_termscondition_title));
		termsConditionDialogueBuilder.setMessage(getResources().getString(R.string.dialogue_termscondition_content));

		termsConditionDialogueBuilder
				.setPositiveButton(getResources().getString(R.string.button_text_got_it), new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				});

		AlertDialog termsConditionDialogue = termsConditionDialogueBuilder.create();
		termsConditionDialogue.show();
	}

	private void openAmsSiteView() {
		String url = getResources().getString(R.string.activity_registration_ams_site);
		Intent intentView = new Intent(Intent.ACTION_VIEW);
		intentView.setData(Uri.parse(url));
		startActivity(intentView);
	}

	private void nextClickAction() {
		showProgress();
		validateLoginPresenter.validateLogin(editTextCompanyName.getText().toString(), editTextPhoneNumber.getText().toString());
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
		CommonUtils.showSnackBar(this, message, findViewById(R.id.activity_registration));
	}

	@Override
	public void onSuccessLogin() {
		hideProgress();
		Log.i(TAG, "onSuccessLogin");
		Intent intent = new Intent(getApplicationContext(), OldPhoneNumberActivity.class);
		startActivity(intent);
	}

	@Override
	public void onCompanyNameNotFound() {
		hideProgress();
		CommonUtils.showSnackBar(this, "Company not found", findViewById(R.id.content_my_geo));
	}

	@Override
	public void onMobileNumberNotFound() {
		hideProgress();
		CommonUtils.showSnackBar(this, "Mobile number not found", findViewById(R.id.content_my_geo));
	}

}
