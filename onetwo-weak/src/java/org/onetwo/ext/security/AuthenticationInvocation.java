package org.onetwo.ext.security;

import org.onetwo.ext.security.config.AuthenticConfig;

@SuppressWarnings("unchecked")
public interface AuthenticationInvocation {
	
	public String NAME = "authentication";

//	public void authenticate(AuthenticConfig config, Authoritable authoritable);
	public Object authenticate(AuthenticConfig config, SecurityTarget target, UserDetail authoritable) throws Exception;
	
}
