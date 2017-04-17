package ams.com.eattendance.domain.interactors;

import ams.com.eattendance.domain.interactors.base.Interactor;

public interface ValidateLoginInteractor extends Interactor {

	interface Callback {

		void onSuccessLogin();

		void onCompanyNameNotFound();

		void onMobileNumberNotFound();

		void onError(String message);
	}
}
