package ams.com.eattendance.domain.interactors.impl;

import java.io.IOException;

import ams.com.eattendance.domain.executor.Executor;
import ams.com.eattendance.domain.executor.MainThread;
import ams.com.eattendance.domain.interactors.ValidateLoginInteractor;
import ams.com.eattendance.domain.interactors.base.AbstractInteractor;
import ams.com.eattendance.domain.repository.WebServiceRepository;

/**
 * Created by WeConnect on 4/13/2017.
 */
public class ValidateLoginInteractorImpl extends AbstractInteractor implements ValidateLoginInteractor {

	private WebServiceRepository webServiceRepository;
	private Callback callback;
	private String companyName;
	private String phoneNumber;

	public ValidateLoginInteractorImpl(Executor threadExecutor, MainThread mainThread,  String companyName, String
			phoneNumber,
			WebServiceRepository webServiceRepository, Callback callback) {
		super(threadExecutor, mainThread);
		this.companyName = companyName;
		this.phoneNumber = phoneNumber;
		this.webServiceRepository = webServiceRepository;
		this.callback = callback;
	}

	@Override
	public void run() {
		try {
			Boolean valid = this.webServiceRepository.getCompanyByName(companyName, phoneNumber);

			if(valid == null) {
				callback.onError("Backend call is unsuccessful");
				return;
			}

			if(valid) {
				callback.onSuccessLogin();
			} else {
				callback.onCompanyNameNotFound();
			}
		} catch (IOException e) {
			callback.onError(e.getMessage());
		}
	}
}
