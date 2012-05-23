package org.onetwo.ext.security.exception;


public class NotLoginException extends AuthenticationException {
	public static final String DEFAULT_MESSAGE = "not login yet[没有登录].";

	/**
	 * 
	 */
	private static final long serialVersionUID = 7280411050853219784L;

	public NotLoginException() {
		super(DEFAULT_MESSAGE);
	}

	public NotLoginException(String message) {
		super(message);
	}

	public NotLoginException(Throwable cause) {
		super(cause);
	}

	public NotLoginException(String message, Throwable cause) {
		super(message, cause);
	}
}
