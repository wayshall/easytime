package org.onetwo.ext.security;

import java.util.List;

import org.onetwo.core.exception.ServiceException;
import org.onetwo.ext.security.config.AuthenticConfig;
import org.onetwo.ext.security.exception.AuthenticationException;
import org.onetwo.ext.security.exception.NoAuthorizationException;
import org.onetwo.ext.security.exception.NotLoginException;

@SuppressWarnings("unchecked")
public class DefaultAuthentication implements AuthenticationInvocation {
	
	public static final String LONGIN_VIEW = "login";
	public static final String LOGIN_REDIRECT = "login_redirect";

	@Override
	public Object authenticate(AuthenticConfig config, SecurityTarget target, UserDetail authoritable) throws Exception {
		if (!config.isNeedAuthencation())
			return target.execute();
		
		if(config.isOnlyAuthenticator()){
			if(config.getAuthenticators()==null)
				throw new ServiceException("Authentic[OnlyAuthenticator=true] but  Authenticator is null.");
			return authenticateByAuthenticator(config, target, authoritable);
		}

		if (!authenticatLogin(authoritable))
			throw new NotLoginException();
		
		if (!authenticatMember(config.getMembers(), authoritable))
			throw new NoAuthorizationException();

		return authenticateByAuthenticator(config, target, authoritable);
	}

	public boolean authenticatLogin(UserDetail authoritable) {
		if (authoritable == null)
			return false;
		return true;
	}
	
	public Object authenticateByAuthenticator(AuthenticConfig config, SecurityTarget target, UserDetail authoritable) throws Exception{
		Object result = null;
		boolean isAllow = true;
		for(Authenticator auth : config.getAuthenticators()){
			if(!(auth instanceof BeforeAuthenticator))
				continue;
			isAllow = invokeBefore((BeforeAuthenticator)auth, config, authoritable, target);
			if(isAllow)
				break;
		}
		if(!isAllow)
			throw new AuthenticationException();
		
		result = target.execute();
		
		for(Authenticator auth : config.getAuthenticators()){
			if(!(auth instanceof AfterAuthenticator))
				continue;
			isAllow = invokeAfter((AfterAuthenticator)auth, config, authoritable, target);
			if(isAllow)
				break;
		}
		if(!isAllow)
			throw new AuthenticationException();
		
		return result;
	}
	
	public boolean invokeBefore(BeforeAuthenticator auth, AuthenticConfig config, UserDetail authoritable, SecurityTarget target){
		try {
			return auth.beforeTarget(config, authoritable, target);
		} catch (AuthenticationException e) {
			target.addMessage(e.getMessage());
		}
		return false;
	}
	
	public boolean invokeAfter(AfterAuthenticator auth, AuthenticConfig config, UserDetail authoritable, SecurityTarget target){
		try {
			return auth.afterTarget(config, authoritable, target);
		} catch (AuthenticationException e) {
			target.addMessage(e.getMessage());
		}
		return false;
	}

	public boolean authenticatMember(List<String> members, UserDetail authoritable) {
		if (members == null || members.isEmpty())
			return true;
		for (String expr : members) {
			boolean isAutheic = false;
			if(expr.startsWith("%{") && expr.endsWith("}")){
				isAutheic = authenticateExpression(expr.substring(2, expr.length()-1), authoritable);
			}else{
				isAutheic = expr.equals(authoritable.getUserId());
			}
			if(isAutheic)
				return true;
		}

		return false;
	}
	
	protected boolean authenticateExpression(String expr, UserDetail authoritable){
		return true;
	}
	
}
