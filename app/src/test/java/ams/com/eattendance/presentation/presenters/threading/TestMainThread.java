package ams.com.eattendance.presentation.presenters.threading;

import ams.com.eattendance.domain.executor.MainThread;

public class TestMainThread implements MainThread {

	@Override
	public void post(Runnable runnable) {
		// tests can run on this thread, no need to invoke other threads
		runnable.run();
	}

}
