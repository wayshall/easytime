package org.onetwo.core.beanfactory;

public interface AfterConstruction<T> {
	
	public void afterConstruction(T bean);

}
