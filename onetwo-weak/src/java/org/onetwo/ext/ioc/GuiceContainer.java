package org.onetwo.ext.ioc;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class GuiceContainer implements Container {
	
	protected Injector injector;
	
	public GuiceContainer(){
	}
	
	@Override
	public void build(Module...modules) {
		this.injector = Guice.createInjector(modules);
	}

	public void inject(Object object) {
		this.injector.injectMembers(object);
	}

	public <T>T getInstance(Class<T> clazz) {
		T bean = this.injector.getInstance(clazz);
		inject(bean);
		return bean;
	}

}
