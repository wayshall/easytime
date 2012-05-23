package org.onetwo.ext.security;

import org.onetwo.ext.security.config.AuthenticConfig;


@SuppressWarnings("unchecked")
public interface AfterAuthenticator  extends Authenticator{

	public boolean afterTarget(AuthenticConfig config, UserDetail authoritable, SecurityTarget target);
	
}
