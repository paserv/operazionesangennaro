package it.osg.runnable.exception;

public class TimeoutException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TimeoutException() {
		super("Queue is in Timeout");
	}
	
}
