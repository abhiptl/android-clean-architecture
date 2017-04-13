package ams.com.eattendance.data.database.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by WeConnect on 4/12/2017.
 */
@Entity(nameInDb = KeyValueConfig.TABLE_NAME)
public class KeyValueConfig {

	protected static final String TABLE_NAME = "KEY_VALUE_CONFIG";

	protected static final String COLUMN_CONFIG_GROUP = "CONFIG_GROUP";
	protected static final String COLUMN_CONFIG_KEY = "CONFIG_KEY";
	protected static final String COLUMN_CONFIG_VALUE = "CONFIG_VALUE";

	@Id(autoincrement = true)
	private Long id;

	@Property(nameInDb = COLUMN_CONFIG_GROUP)
	private String configGroup;

	@Property(nameInDb = COLUMN_CONFIG_KEY)
	private String configKey;

	@Property(nameInDb = COLUMN_CONFIG_VALUE)
	private String configValue;

	@Generated(hash = 1816822071)
	public KeyValueConfig(Long id, String configGroup, String configKey,
			String configValue) {
		this.id = id;
		this.configGroup = configGroup;
		this.configKey = configKey;
		this.configValue = configValue;
	}

	@Generated(hash = 440130338)
	public KeyValueConfig() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getConfigGroup() {
		return this.configGroup;
	}

	public void setConfigGroup(String configGroup) {
		this.configGroup = configGroup;
	}

	public String getConfigKey() {
		return this.configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public String getConfigValue() {
		return this.configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}


}
