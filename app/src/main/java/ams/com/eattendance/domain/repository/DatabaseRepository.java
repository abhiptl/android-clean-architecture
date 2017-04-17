package ams.com.eattendance.domain.repository;

import ams.com.eattendance.domain.model.Geofence;

/**
 * <p>
 * This repository will get data from local database.
 * </p>
 */
public interface DatabaseRepository {

	public Geofence getGeofence(final long employeeId);
}
