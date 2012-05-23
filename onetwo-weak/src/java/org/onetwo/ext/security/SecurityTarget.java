package org.onetwo.ext.security;

public interface SecurityTarget {
	
	public Object execute() throws Exception;
	
	public SecurityTarget addMessage(String msg);
	
	public Object getTarget();
	
}
