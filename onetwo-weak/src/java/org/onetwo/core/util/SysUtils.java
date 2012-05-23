package org.onetwo.core.util;

import org.onetwo.core.exception.ServiceException;

public abstract class SysUtils {

	public static void handleException(Exception e){
		if(e instanceof ServiceException){
			throw (ServiceException) e;
		}else{
			throw new ServiceException(e);
		}
	}
}
