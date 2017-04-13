package ams.com.eattendance.presentation.presenters.base;

import ams.com.eattendance.domain.executor.Executor;
import ams.com.eattendance.domain.executor.MainThread;

/**
 * This is a base class for all presenters which are communicating with interactors. This base class will hold a
 * reference to the Executor and MainThread objects that are needed for running interactors in a background thread.
 */
public abstract class AbstractPresenter {

	protected Executor executor;
	protected MainThread mainThread;

	public AbstractPresenter(Executor executor, MainThread mainThread) {
		this.executor = executor;
		this.mainThread = mainThread;
	}
}
