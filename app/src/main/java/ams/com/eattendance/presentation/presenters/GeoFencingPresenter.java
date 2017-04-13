package ams.com.eattendance.presentation.presenters;

import ams.com.eattendance.domain.model.Geofence;
import ams.com.eattendance.presentation.presenters.base.BasePresenter;
import ams.com.eattendance.presentation.ui.BaseView;

/**
 * Created by WeConnect on 4/10/2017.
 */
public interface GeoFencingPresenter extends BasePresenter {

	void getGeoFencing(final long employeeId);

	interface View extends BaseView {

		void geoFencingRetrieved(Geofence geofence);

		void geoFencingNotFound();
	}
}
