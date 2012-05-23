package org.onetwo.core.exception;



public class ServiceException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7280411050853219784L;
	
	public ServiceException() {
		super();
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
