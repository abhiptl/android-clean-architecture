package ams.com.eattendance.presentation.threading;

import ams.com.eattendance.domain.executor.MainThread;
import android.os.Handler;
import android.os.Looper;

/**
 * This class makes sure that the runnable we provide will be run on the main UI thread.
 * <p/>
 * Created by dmilicic on 7/29/15.
 */
public class MainThreadImpl implements MainThread {

	private static MainThread mainThread;

	private Handler handler;

	private MainThreadImpl() {
		handler = new Handler(Looper.getMainLooper());
	}

	public static MainThread getInstance() {
		if (mainThread == null) {
			mainThread = new MainThreadImpl();
		}

		return mainThread;
	}

	@Override
	public void post(Runnable runnable) {
		handler.post(runnable);
	}
}
