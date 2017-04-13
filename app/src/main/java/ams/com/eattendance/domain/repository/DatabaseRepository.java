package ams.com.eattendance.domain.repository;

import ams.com.eattendance.domain.model.Geofence;

/**
 * Created by WeConnect on 4/10/2017.
 * <p>
 *     This repository will get data from local database.
 * </p>
 */
public interface DatabaseRepository {

	public Geofence getGeofence(final long employeeId);
}
