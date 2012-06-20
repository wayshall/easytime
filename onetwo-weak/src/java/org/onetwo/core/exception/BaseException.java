package org.onetwo.core.exception;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class BaseException extends RuntimeException {

	protected static final String DefaultMsg = "occur error";
	protected static final String Prefix = "[ERROR]:";
	protected int code;

	protected List<Throwable> exceptions = new ArrayList<Throwable>(0);

	public BaseException() {
		super(DefaultMsg);
		init();
	}

	public BaseException(String msg) {
		super(Prefix + msg);
		init();
	}

	public BaseException(String msg, int code) {
		this(msg);
		this.code = code;
		init();
	}

	public BaseException(Throwable cause) {
		super(DefaultMsg, cause);
		init();
	}

	public BaseException(Throwable cause, int code) {
		this(cause);
		this.code = code;
		init();
	}

	public BaseException(String msg, Throwable cause) {
		super("[" + cause.getMessage() + "] : " + msg, cause);
		init();
	}

	public BaseException(String msg, Throwable cause, int code) {
		this(msg, cause);
		this.code = code;
		init();
	}

	public int getCode() {
		return code;
	}

	protected void init() {
		this.exceptions.add(this);
	}

}
