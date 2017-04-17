package ams.com.eattendance.domain.executor.impl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ams.com.eattendance.domain.executor.Executor;
import ams.com.eattendance.domain.interactors.base.AbstractInteractor;

/**
 * This singleton class will make sure that each interactor operation gets a background thread.
 */
public class ThreadExecutor implements Executor {

	private static final int CORE_POOL_SIZE = 3;
	private static final int MAX_POOL_SIZE = 5;
	private static final int KEEP_ALIVE_TIME = 120;
	private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
	private static final BlockingQueue<Runnable> WORK_QUEUE = new LinkedBlockingQueue<Runnable>();

	// This is a singleton
	private static volatile ThreadExecutor threadExecutor;
	private ThreadPoolExecutor threadPoolExecutor;

	private ThreadExecutor() {
		long keepAlive = KEEP_ALIVE_TIME;
		threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, keepAlive, TIME_UNIT, WORK_QUEUE);
	}

	/**
	 * Returns a singleton instance of this executor. If the executor is not initialized then it initializes it and returns
	 * the instance.
	 */
	public static Executor getInstance() {
		if (threadExecutor == null) {
			threadExecutor = new ThreadExecutor();
		}

		return threadExecutor;
	}

	@Override
	public void execute(final AbstractInteractor interactor) {
		threadPoolExecutor.submit(new Runnable() {

			@Override
			public void run() {
				// run the main logic
				interactor.run();

				// mark it as finished
				interactor.onFinished();
			}
		});
	}
}
