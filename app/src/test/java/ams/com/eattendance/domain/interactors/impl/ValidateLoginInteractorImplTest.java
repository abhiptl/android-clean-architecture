package ams.com.eattendance.domain.interactors.impl;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import ams.com.eattendance.domain.executor.Executor;
import ams.com.eattendance.domain.executor.MainThread;
import ams.com.eattendance.domain.interactors.ValidateLoginInteractor;
import ams.com.eattendance.domain.repository.WebServiceRepository;
import ams.com.eattendance.presentation.presenters.threading.TestMainThread;

@RunWith(MockitoJUnitRunner.class)
public class ValidateLoginInteractorImplTest {

	private static String TEST_COMPANY_NAME = "company_name";
	private static String TEST_PHONE_NUMBER = "1234567890";
	@Mock
	private WebServiceRepository webServiceRepository;
	@Mock
	private ValidateLoginInteractor.Callback callback;
	private MainThread mainThread;
	@Mock
	private Executor executor;
	private ValidateLoginInteractorImpl validateLoginInteractor;

	@Before
	public void setUp() throws Exception {
		mainThread = new TestMainThread();
	}

	@Test
	public void testRun_company_phone_found() throws Exception {
		Mockito.when(webServiceRepository.getCompanyByName(TEST_COMPANY_NAME, TEST_PHONE_NUMBER)).thenReturn(true);

		validateLoginInteractor =
				new ValidateLoginInteractorImpl(executor, mainThread, TEST_COMPANY_NAME, TEST_PHONE_NUMBER, webServiceRepository, callback);
		validateLoginInteractor.run();

		Mockito.verify(callback).onSuccessLogin();

	}

	@Test
	public void testRun_company_not_found() throws Exception {
		Mockito.when(webServiceRepository.getCompanyByName(TEST_COMPANY_NAME, TEST_PHONE_NUMBER)).thenReturn(false);

		validateLoginInteractor =
				new ValidateLoginInteractorImpl(executor, mainThread, TEST_COMPANY_NAME, TEST_PHONE_NUMBER, webServiceRepository, callback);
		validateLoginInteractor.run();

		Mockito.verify(callback).onCompanyNameNotFound();

	}

	@Test
	public void testRun_not_successful_return() throws Exception {
		Mockito.when(webServiceRepository.getCompanyByName(TEST_COMPANY_NAME, TEST_PHONE_NUMBER)).thenReturn(null);

		validateLoginInteractor =
				new ValidateLoginInteractorImpl(executor, mainThread, TEST_COMPANY_NAME, TEST_PHONE_NUMBER, webServiceRepository, callback);
		validateLoginInteractor.run();

		Mockito.verify(callback).onError("Backend call is unsuccessful");
	}

	@Test
	public void testRun_throw_exception() throws Exception {
		Mockito.when(webServiceRepository.getCompanyByName(TEST_COMPANY_NAME, TEST_PHONE_NUMBER))
				.thenThrow(new IOException("SocketTimeout"));

		validateLoginInteractor =
				new ValidateLoginInteractorImpl(executor, mainThread, TEST_COMPANY_NAME, TEST_PHONE_NUMBER, webServiceRepository, callback);
		validateLoginInteractor.run();

		Mockito.verify(callback).onError("SocketTimeout");
	}

}