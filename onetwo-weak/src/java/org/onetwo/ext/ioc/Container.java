package org.onetwo.ext.ioc;

import com.google.inject.Module;

public interface Container {

	public void build(Module...modules);
	
	public void inject(Object object);
	
	public <T>T getInstance(Class<T> clazz);
	
}
