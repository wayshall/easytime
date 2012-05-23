package org.onetwo.ext.security;

import org.onetwo.ext.security.config.AuthenticConfig;


@SuppressWarnings("unchecked")
public interface BeforeAuthenticator extends Authenticator{
	
	public boolean beforeTarget(AuthenticConfig config, UserDetail authoritable, SecurityTarget target);
	
}
