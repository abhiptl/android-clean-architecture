package ams.com.eattendance.presentation.presenters.impl;

import ams.com.eattendance.domain.executor.Executor;
import ams.com.eattendance.domain.executor.MainThread;
import ams.com.eattendance.domain.interactors.ValidateLoginInteractor;
import ams.com.eattendance.domain.interactors.impl.ValidateLoginInteractorImpl;
import ams.com.eattendance.domain.repository.WebServiceRepository;
import ams.com.eattendance.presentation.presenters.ValidateLoginPresenter;
import ams.com.eattendance.presentation.presenters.base.AbstractPresenter;

/**
 * Created by WeConnect on 4/13/2017.
 */
public class ValidateLoginPresenterImpl extends AbstractPresenter implements ValidateLoginPresenter, ValidateLoginInteractor.Callback {

	private ValidateLoginPresenter.View view;
	private WebServiceRepository webServiceRepository;

	public ValidateLoginPresenterImpl(Executor executor, MainThread mainThread,ValidateLoginPresenter.View view, WebServiceRepository
			webServiceRepository) {
		super(executor, mainThread);
		this.view = view;
		this.webServiceRepository = webServiceRepository;
	}

	@Override
	public void resume() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void stop() {

	}

	@Override
	public void destroy() {

	}

	@Override
	public void onError(String message) {
		view.showError(message);
	}

	@Override
	public void onSuccessLogin() {
		view.onSuccessLogin();
	}

	@Override
	public void onCompanyNameNotFound() {
		view.onCompanyNameNotFound();
	}

	@Override
	public void onMobileNumberNotFound() {
		view.onMobileNumberNotFound();
	}


	@Override
	public void validateLogin(String companyName, String phoneNumber) {
		ValidateLoginInteractor validateLoginInteractor = new ValidateLoginInteractorImpl(executor, mainThread, companyName, phoneNumber,
				webServiceRepository, this);
		validateLoginInteractor.execute();
	}
}
