package ams.com.eattendance.presentation.presenters.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import ams.com.eattendance.domain.executor.MainThread;
import ams.com.eattendance.domain.executor.impl.TestThreadExecutor;
import ams.com.eattendance.domain.model.Geofence;
import ams.com.eattendance.domain.repository.DatabaseRepository;
import ams.com.eattendance.presentation.presenters.GeoFencingPresenter;
import ams.com.eattendance.presentation.presenters.threading.TestMainThread;

@RunWith(MockitoJUnitRunner.class)
public class GeoFencingPresenterImplTest {

	private static long TEST_EMPLOYEE_ID = 100;
	@Mock
	private GeoFencingPresenter.View view;
	@Mock
	private DatabaseRepository databaseRepository;
	private GeoFencingPresenter geoFencingPresenter;
	private MainThread mainThread;
	/**
	 * Use TestThreadExecutor instead of ThreadExecutor designed for unit/integration testing.
	 */
	private TestThreadExecutor executor;

	@Before
	public void setUp() throws Exception {
		mainThread = new TestMainThread();
		executor = (TestThreadExecutor) TestThreadExecutor.getInstance();
		geoFencingPresenter = new GeoFencingPresenterImpl(executor, mainThread, view, databaseRepository);
	}

	@Test
	public void test_getGeoFencing_successful() throws Exception {
		Geofence geofence = new Geofence("location", 1.00000, 1.00000, 15.0f);
		Mockito.when(databaseRepository.getGeofence(TEST_EMPLOYEE_ID)).thenReturn(geofence);

		geoFencingPresenter.getGeoFencing(TEST_EMPLOYEE_ID);

		executor.waitForAllSubmittedTasksToComplete();

		Mockito.verify(view).geoFencingRetrieved(geofence);
	}

	@Test
	public void test_getGeoFencing_not_found() throws Exception {
		Mockito.when(databaseRepository.getGeofence(TEST_EMPLOYEE_ID)).thenReturn(null);

		geoFencingPresenter.getGeoFencing(TEST_EMPLOYEE_ID);

		executor.waitForAllSubmittedTasksToComplete();

		Mockito.verify(view).geoFencingNotFound();
	}

}