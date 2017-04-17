package ams.com.eattendance.presentation.presenters.impl;

import ams.com.eattendance.domain.executor.Executor;
import ams.com.eattendance.domain.executor.MainThread;
import ams.com.eattendance.domain.interactors.GetGeoFencingInteractor;
import ams.com.eattendance.domain.interactors.impl.GetGeoFencingInteractorImpl;
import ams.com.eattendance.domain.model.Geofence;
import ams.com.eattendance.domain.repository.DatabaseRepository;
import ams.com.eattendance.presentation.presenters.GeoFencingPresenter;
import ams.com.eattendance.presentation.presenters.base.AbstractPresenter;

public class GeoFencingPresenterImpl extends AbstractPresenter implements GeoFencingPresenter, GetGeoFencingInteractor.Callback {

	private GeoFencingPresenter.View view;
	private DatabaseRepository databaseRepository;

	public GeoFencingPresenterImpl(Executor executor, MainThread mainThread, GeoFencingPresenter.View view,
			DatabaseRepository databaseRepository) {
		super(executor, mainThread);
		this.view = view;
		this.databaseRepository = databaseRepository;
	}

	@Override
	public void getGeoFencing(long employeeId) {
		GetGeoFencingInteractor getGeoFencingInteractor =
				new GetGeoFencingInteractorImpl(executor, mainThread, employeeId, databaseRepository, this);
		getGeoFencingInteractor.execute();

	}

	@Override
	public void onGeoFencingRetrieved(Geofence geofence) {
		this.view.geoFencingRetrieved(geofence);
	}

	@Override
	public void onGeoFencingNotFound() {
		this.view.geoFencingNotFound();
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

	}
}
