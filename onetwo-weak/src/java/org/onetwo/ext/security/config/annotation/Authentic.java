package org.onetwo.ext.security.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Authentic {
	String[] members() default {};
	String[] authenticator() default {};
	boolean isOnlyAuthenticator() default false;
	boolean ignore() default false;
}
