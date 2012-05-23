package org.onetwo.ext.security.exception;

import org.onetwo.core.exception.ServiceException;


@SuppressWarnings({"unchecked","serial"})
public class AuthenticationException extends ServiceException {
	public static final String DEFAULT_MESSAGE = "Authentication fail[验证失败]:";

 
	public AuthenticationException() {
		super(DEFAULT_MESSAGE);
	}

	public AuthenticationException(String message) {
		super(DEFAULT_MESSAGE+message);
	}

	public AuthenticationException(Throwable cause) {
		super(cause);
	}

	public AuthenticationException(String message, Throwable cause) {
		super(DEFAULT_MESSAGE+message, cause);
	}
}
