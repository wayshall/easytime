package org.onetwo.core.weak.exception.db;

import org.onetwo.core.exception.BaseException;

@SuppressWarnings("serial")
public class QueryException extends BaseException {

	private static final String DefaultMsg = "query error";
	
	public QueryException(){
		super(DefaultMsg);
	}
	
	public QueryException(String msg){
		super(msg);
	}
	
	public QueryException(Throwable cause){
		super(DefaultMsg, cause);
	}
	
	public QueryException(String msg, Throwable cause){
		super(msg, cause);
	}
}
