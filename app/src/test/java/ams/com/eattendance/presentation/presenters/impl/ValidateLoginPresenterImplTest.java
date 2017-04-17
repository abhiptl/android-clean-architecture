package ams.com.eattendance.presentation.presenters.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import ams.com.eattendance.domain.executor.MainThread;
import ams.com.eattendance.domain.executor.impl.TestThreadExecutor;
import ams.com.eattendance.domain.repository.WebServiceRepository;
import ams.com.eattendance.presentation.presenters.ValidateLoginPresenter;
import ams.com.eattendance.presentation.presenters.threading.TestMainThread;

@RunWith(MockitoJUnitRunner.class)
public class ValidateLoginPresenterImplTest {

	private static String TEST_COMPANY_NAME = "company_name";
	private static String TEST_PHONE_NUMBER = "1234567890";
	@Mock
	private ValidateLoginPresenter.View view;
	@Mock
	private WebServiceRepository webServiceRepository;
	private MainThread mainThread;
	private TestThreadExecutor executor;
	private ValidateLoginPresenter validateLoginPresenter;

	@Before
	public void setUp() throws Exception {
		mainThread = new TestMainThread();
		executor = (TestThreadExecutor) TestThreadExecutor.getInstance();
	}

	@Test
	public void testValidateLogin_successful() throws Exception {
		Mockito.when(webServiceRepository.getCompanyByName(TEST_COMPANY_NAME, TEST_PHONE_NUMBER)).thenReturn(true);

		validateLoginPresenter = new ValidateLoginPresenterImpl(executor, mainThread, view, webServiceRepository);
		validateLoginPresenter.validateLogin(TEST_COMPANY_NAME, TEST_PHONE_NUMBER);

		executor.waitForAllSubmittedTasksToComplete();

		Mockito.verify(view).onSuccessLogin();
	}

	@Test
	public void testValidateLogin_company_not_found() throws Exception {
		Mockito.when(webServiceRepository.getCompanyByName(TEST_COMPANY_NAME, TEST_PHONE_NUMBER)).thenReturn(false);

		validateLoginPresenter = new ValidateLoginPresenterImpl(executor, mainThread, view, webServiceRepository);
		validateLoginPresenter.validateLogin(TEST_COMPANY_NAME, TEST_PHONE_NUMBER);

		executor.waitForAllSubmittedTasksToComplete();

		Mockito.verify(view).onCompanyNameNotFound();
	}

	@Test
	public void testValidateLogin_error() throws Exception {
		Mockito.when(webServiceRepository.getCompanyByName(TEST_COMPANY_NAME, TEST_PHONE_NUMBER)).thenReturn(null);

		validateLoginPresenter = new ValidateLoginPresenterImpl(executor, mainThread, view, webServiceRepository);
		validateLoginPresenter.validateLogin(TEST_COMPANY_NAME, TEST_PHONE_NUMBER);

		executor.waitForAllSubmittedTasksToComplete();

		Mockito.verify(view).showError("Backend call is unsuccessful");
	}

}