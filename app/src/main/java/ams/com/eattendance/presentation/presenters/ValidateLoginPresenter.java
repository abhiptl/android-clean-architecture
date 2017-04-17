package ams.com.eattendance.presentation.presenters;

import ams.com.eattendance.presentation.presenters.base.BasePresenter;
import ams.com.eattendance.presentation.ui.BaseView;

public interface ValidateLoginPresenter extends BasePresenter {

	void validateLogin(String companyName, String phoneNumber);

	interface View extends BaseView {

		void onSuccessLogin();

		void onCompanyNameNotFound();

		void onMobileNumberNotFound();
	}
}
