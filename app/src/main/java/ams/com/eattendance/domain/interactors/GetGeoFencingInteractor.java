package ams.com.eattendance.domain.interactors;

import ams.com.eattendance.domain.interactors.base.Interactor;
import ams.com.eattendance.domain.model.Geofence;

/**
 * Created by WeConnect on 4/10/2017.
 * <p/>
 * This interactor is responsible for retrieving a Geofence data of employee from the database.
 */
public interface GetGeoFencingInteractor extends Interactor {

	interface Callback {

		void onGeoFencingRetrieved(Geofence geofence);

		void onGeoFencingNotFound();
	}
}
