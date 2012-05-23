package org.onetwo.ext.security.exception;


public class NoAuthorizationException extends AuthenticationException {

	public static final String DEFAULT_MESSAGE = "no authorization[没有权限].";
	/**
	 * 
	 */
	private static final long serialVersionUID = 7280411050853219784L;

	public NoAuthorizationException() {
		super(DEFAULT_MESSAGE);
	}

	public NoAuthorizationException(String message) {
		super(message);
	}

	public NoAuthorizationException(Throwable cause) {
		super(cause);
	}

	public NoAuthorizationException(String message, Throwable cause) {
		super(message, cause);
	}
}
