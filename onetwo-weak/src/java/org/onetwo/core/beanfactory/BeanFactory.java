package org.onetwo.core.beanfactory;


@SuppressWarnings("unchecked")
public interface BeanFactory {

	public Object getBean(String className, boolean singleton, AfterConstruction after);
	
	public <T> T getBean(Class<T> clazz, boolean singleton);
	
	public <T> T getBean(Class<T> clazz, boolean singleton, AfterConstruction after);
	
	public void putInCache(Object bean);
	
	public <T> T getBeanFromCache(Class<T> clazz);
	
	public <T> T getBeanFromCache(String className);
	
}
