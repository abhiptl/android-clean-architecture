package ams.com.eattendance.data.database.impl;

import java.util.List;

import org.greenrobot.greendao.query.QueryBuilder;

import ams.com.eattendance.data.database.constant.ConfigGroupEnum;
import ams.com.eattendance.data.database.constant.ConfigKeyEnum;
import ams.com.eattendance.data.database.model.DaoSession;
import ams.com.eattendance.data.database.model.KeyValueConfig;
import ams.com.eattendance.data.database.model.KeyValueConfigDao;
import ams.com.eattendance.domain.model.Geofence;
import ams.com.eattendance.domain.repository.DatabaseRepository;

public class DatabaseRepositoryImpl implements DatabaseRepository {

	private DaoSession daoSession;

	public DatabaseRepositoryImpl(DaoSession daoSession) {
		this.daoSession = daoSession;
	}

	@Override
	public Geofence getGeofence(long employeeId) {
		QueryBuilder<KeyValueConfig> keyValueConfigQueryBuilder = daoSession.getKeyValueConfigDao().queryBuilder();
		keyValueConfigQueryBuilder.where(KeyValueConfigDao.Properties.ConfigGroup.eq(ConfigGroupEnum.GEO_CONFIG));
		List<KeyValueConfig> list = keyValueConfigQueryBuilder.list();
		Geofence geofence = buildGeoFenceModel(list);
		return geofence;

	}

	private Geofence buildGeoFenceModel(List<KeyValueConfig> list) {
		Geofence geofence = null;
		String locationName = null;
		Double latitude = null;
		Double longitude = null;
		Double radius = null;
		for (KeyValueConfig keyValueConfig : list) {
			if (keyValueConfig.getConfigKey().equals(ConfigKeyEnum.LOCATION_NAME.name())) {
				locationName = keyValueConfig.getConfigValue();
			}

			if (keyValueConfig.getConfigKey().equals(ConfigKeyEnum.LATITUDE.name())) {
				latitude = Double.valueOf(keyValueConfig.getConfigValue());
			}

			if (keyValueConfig.getConfigKey().equals(ConfigKeyEnum.LONGITUDE.name())) {
				longitude = Double.valueOf(keyValueConfig.getConfigValue());
			}

			if (keyValueConfig.getConfigKey().equals(ConfigKeyEnum.RADIUS.name())) {
				radius = Double.valueOf(keyValueConfig.getConfigValue());
			}
		}

		if (locationName != null && latitude != null && longitude != null && radius != null) {
			geofence = new Geofence(locationName, latitude, longitude, radius);
		}

		return geofence;
	}

}
