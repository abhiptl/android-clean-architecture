package ams.com.eattendance.domain.interactors.impl;

import ams.com.eattendance.domain.executor.Executor;
import ams.com.eattendance.domain.executor.MainThread;
import ams.com.eattendance.domain.interactors.GetGeoFencingInteractor;
import ams.com.eattendance.domain.interactors.base.AbstractInteractor;
import ams.com.eattendance.domain.model.Geofence;
import ams.com.eattendance.domain.repository.DatabaseRepository;

/**
 * Created by WeConnect on 4/10/2017.
 */
public class GetGeoFencingInteractorImpl extends AbstractInteractor implements GetGeoFencingInteractor {

	private long employeeId;
	private DatabaseRepository databaseRepository;
	private Callback callback;

	public GetGeoFencingInteractorImpl(Executor threadExecutor, MainThread mainThread, long employeeId,
			DatabaseRepository databaseRepository, Callback callback) {
		super(threadExecutor, mainThread);
		this.employeeId = employeeId;
		this.databaseRepository = databaseRepository;
		this.callback = callback;

	}

	@Override
	public void run() {
		final Geofence geofence = databaseRepository.getGeofence(employeeId);

		if(geofence == null) {
			mainThread.post(new Runnable() {

				@Override
				public void run() {
					callback.onGeoFencingNotFound();
				}
			});

		} else {
			mainThread.post(new Runnable() {

				@Override
				public void run() {
					callback.onGeoFencingRetrieved(geofence);
				}
			});
		}

	}
}
