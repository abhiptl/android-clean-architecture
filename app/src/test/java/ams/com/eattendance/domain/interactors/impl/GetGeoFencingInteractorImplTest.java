package ams.com.eattendance.domain.interactors.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import ams.com.eattendance.domain.executor.Executor;
import ams.com.eattendance.domain.executor.MainThread;
import ams.com.eattendance.domain.interactors.GetGeoFencingInteractor;
import ams.com.eattendance.domain.model.Geofence;
import ams.com.eattendance.domain.repository.DatabaseRepository;
import ams.com.eattendance.presentation.presenters.threading.TestMainThread;

@RunWith(MockitoJUnitRunner.class)
public class GetGeoFencingInteractorImplTest {

	private static long TEST_EMPLOYEE_ID = 100;
	GetGeoFencingInteractorImpl getGeoFencingInteractor;
	@Mock
	private DatabaseRepository databaseRepository;
	@Mock
	private GetGeoFencingInteractor.Callback callback;
	private MainThread mainThread;
	@Mock
	private Executor executor;
	private Geofence geofence = null;

	@Before
	public void setUp() throws Exception {
		mainThread = new TestMainThread();
		geofence = new Geofence("location", 1.00000, 1.00000, 15.0f);
	}

	@Test
	public void testRun_geofence_retrieved_successfully() throws Exception {
		Mockito.when(databaseRepository.getGeofence(TEST_EMPLOYEE_ID)).thenReturn(geofence);

		getGeoFencingInteractor = new GetGeoFencingInteractorImpl(executor, mainThread, TEST_EMPLOYEE_ID, databaseRepository, callback);
		getGeoFencingInteractor.run();

		Mockito.verify(callback).onGeoFencingRetrieved(geofence);
	}

	@Test
	public void testRun_geofence_not_found() throws Exception {
		Mockito.when(databaseRepository.getGeofence(TEST_EMPLOYEE_ID)).thenReturn(null);

		getGeoFencingInteractor = new GetGeoFencingInteractorImpl(executor, mainThread, TEST_EMPLOYEE_ID, databaseRepository, callback);
		getGeoFencingInteractor.run();

		Mockito.verify(callback).onGeoFencingNotFound();
	}

}