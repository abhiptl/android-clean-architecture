package ams.com.eattendance.domain.executor.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ams.com.eattendance.domain.executor.Executor;
import ams.com.eattendance.domain.interactors.base.AbstractInteractor;

/**
 * TestThreadExecutor for writing unit/integration test.
 * <p>
 * This class is same as ThreadExecutor with changes to public constructor to create its instance and method to get ThreadPoolExecutor.
 * <p>
 * Presenter may submit multiple interactors. All  interactors will run in background in separate thread and call presenter call back.
 * To verify(Mockito) which callback(of presenters) called, all presenters needs to run successfully and then tests can verify
 * callback being called by interactor.
 */
public class TestThreadExecutor implements Executor {

	private static final int CORE_POOL_SIZE = 3;
	private static final int MAX_POOL_SIZE = 5;
	private static final int KEEP_ALIVE_TIME = 120;
	private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
	private static final BlockingQueue<Runnable> WORK_QUEUE = new LinkedBlockingQueue<Runnable>();
	private static final List<Future<?>> futures = new ArrayList<Future<?>>();

	// This is a singleton
	private static volatile TestThreadExecutor threadExecutor;
	private ThreadPoolExecutor threadPoolExecutor;

	/**
	 * Constructor of TestThreadExecutor kept as public so every test method will create new instance of it and so new ThreadPoolExecutor
	 * will be created.
	 * <p>
	 * Every test method creates new ThreadPool, run the presenter and shutdown the created ThreadPool to verify called callback of
	 * presenter.
	 */
	private TestThreadExecutor() {
		long keepAlive = KEEP_ALIVE_TIME;
		threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, keepAlive, TIME_UNIT, WORK_QUEUE);
	}

	/**
	 * Returns a singleton instance of this executor. If the executor is not initialized then it initializes it and returns
	 * the instance.
	 */
	public static Executor getInstance() {
		if (threadExecutor == null) {
			threadExecutor = new TestThreadExecutor();
		}

		return threadExecutor;
	}

	@Override
	public void execute(final AbstractInteractor interactor) {
		Future<?> submit = threadPoolExecutor.submit(new Runnable() {

			@Override
			public void run() {
				// run the main logic
				interactor.run();

				// mark it as finished
				interactor.onFinished();
			}
		});

		futures.add(submit);

	}

	/**
	 * This method waits for all the tasks submitted to thread pool and called by test cases before verifying mock.
	 */
	public void waitForAllSubmittedTasksToComplete() {
		for (Future future : futures) {
			try {
				future.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

}
