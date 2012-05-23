package org.onetwo.ext.security;

import java.io.Serializable;


public interface UserDetail<ID extends Serializable> extends Serializable { 
	
	public final static String USER_DETAIL_KEY = "userLoginInfo"; 
	
	public String getUserName();
	
	public Long getUserId();

}
