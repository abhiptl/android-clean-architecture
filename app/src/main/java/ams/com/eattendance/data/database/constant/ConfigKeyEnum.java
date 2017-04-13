package ams.com.eattendance.data.database.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WeConnect on 4/12/2017.
 */
public enum ConfigKeyEnum {

	LATITUDE(ConfigGroupEnum.GEO_CONFIG),
	LONGITUDE(ConfigGroupEnum.GEO_CONFIG),
	LOCATION_NAME(ConfigGroupEnum.GEO_CONFIG),
	RADIUS(ConfigGroupEnum.GEO_CONFIG);

	private ConfigGroupEnum configGroupEnum;


	ConfigKeyEnum(ConfigGroupEnum configGroupEnum) {
		this.configGroupEnum = configGroupEnum;
	}

	public ConfigGroupEnum getConfigGroupEnum() {
		return this.configGroupEnum;
	}

	public List<ConfigKeyEnum> getConfigKeysByGroup(ConfigGroupEnum configGroupEnum) {
		List<ConfigKeyEnum> list = new ArrayList<>();

		for(ConfigKeyEnum configKeyEnum : ConfigKeyEnum.values()) {
			if(configKeyEnum.getConfigGroupEnum().equals(configGroupEnum)) {
				list.add(configKeyEnum);
			}
		}

		return  list;
	}
}
