package ams.com.eattendance.domain.interactors;

import ams.com.eattendance.domain.interactors.base.Interactor;

/**
 * Created by WeConnect on 4/13/2017.
 */
public interface ValidateLoginInteractor extends Interactor {

	interface Callback {
		void onSuccessLogin();
		void onCompanyNameNotFound();
		void onMobileNumberNotFound();
		void onError(String message);
	}
}
