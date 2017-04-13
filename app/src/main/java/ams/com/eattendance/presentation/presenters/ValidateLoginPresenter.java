package ams.com.eattendance.presentation.presenters;

import ams.com.eattendance.presentation.presenters.base.BasePresenter;
import ams.com.eattendance.presentation.ui.BaseView;

/**
 * Created by WeConnect on 4/13/2017.
 */
public interface ValidateLoginPresenter  extends BasePresenter {

	void validateLogin(String companyName, String phoneNumber);

	interface View extends BaseView {
		void onSuccessLogin();
		void onCompanyNameNotFound();
		void onMobileNumberNotFound();
	}
}
