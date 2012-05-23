package org.onetwo.ext.security.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.onetwo.core.util.ReflectUtils;
import org.onetwo.ext.security.AnnotationUtils;
import org.onetwo.ext.security.BeforeAuthenticator;
import org.onetwo.ext.security.config.annotation.Authentic;

@SuppressWarnings("unchecked")
public class ConfigBuilder {

	private Class<?> clazz;
	private Method method;
	private AuthenticConfig config;

	public ConfigBuilder(Class<?> clazz, Method method) {
		this.clazz = clazz;
		this.method = method;
	}

	public AuthenticConfig buildAuthenConfig() {
		Authentic authentic = findAnnotation(clazz, method, Authentic.class);
		if (authentic == null || authentic.ignore()) {
			config = AuthenticConfig.DEFAULT_CONIFG;
			return config;
		}

		config = new AuthenticConfig(method.toGenericString(), authentic.isOnlyAuthenticator());
		buildAuthenticator(authentic);
		buildMemberConfig(authentic);

		return config;
	}

	protected void buildMemberConfig(Authentic authentic) {
		String[] members = authentic.members();
		if (members != null && members.length > 0) {
			for (String member : members)
				config.addMember(member);
		}
	}

	protected void buildAuthenticator(Authentic authentic) {
		String[] authenticatorNames = authentic.authenticator();
		if (authenticatorNames != null && authenticatorNames.length > 0) {
			for (String name : authenticatorNames) {
				BeforeAuthenticator authenticator = getBean(BeforeAuthenticator.class, name);
				config.addAuthenticator(authenticator);
			}
		}
	}
	
	protected <T> T getBean(Class<T> clazz, String name){
		T bean = (T) ReflectUtils.newInstance(name);
		return bean;
	}

	protected <T extends Annotation> T findAnnotation(Class<?> clazz, Method method, Class<T> annotationClass) {
		return AnnotationUtils.findAnnotation(clazz, method, annotationClass, null);
	}

}
