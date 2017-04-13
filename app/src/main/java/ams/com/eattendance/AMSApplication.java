package ams.com.eattendance;

import org.greenrobot.greendao.database.Database;

import ams.com.eattendance.data.database.model.DaoMaster;
import ams.com.eattendance.data.database.model.DaoSession;
import android.app.Application;

/**
 * Created by WeConnect on 3/28/2017.
 */
public class AMSApplication extends Application {

	/** A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher. */
	public static final boolean ENCRYPTED = false;

	private static final String SUPER_SECRETE = "super-secret";
	private static final String DB_NAME = "ams-db";
	private static final String ENCRYPTED_DB_NAME = "ams-db-encrypted";

	private DaoSession daoSession;


	@Override
	public void onCreate() {
		super.onCreate();

		DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? ENCRYPTED_DB_NAME : DB_NAME);
		Database db = ENCRYPTED ? helper.getEncryptedWritableDb(SUPER_SECRETE) : helper.getWritableDb();
		daoSession = new DaoMaster(db).newSession();

	}

	public DaoSession getDaoSession() {
		return daoSession;
	}
}
