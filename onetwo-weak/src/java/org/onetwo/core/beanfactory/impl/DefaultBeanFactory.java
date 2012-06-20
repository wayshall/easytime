package org.onetwo.core.beanfactory.impl;

import java.util.HashMap;
import java.util.Map;

import org.onetwo.core.beanfactory.AfterConstruction;
import org.onetwo.core.beanfactory.BeanFactory;
import org.onetwo.core.beanfactory.BeanFactoryAware;
import org.onetwo.core.beanfactory.InitializeBean;
import org.onetwo.core.exception.ServiceException;
import org.onetwo.core.util.ReflectUtils;

@SuppressWarnings("unchecked")
public class DefaultBeanFactory implements BeanFactory {

	protected Map<String, Object> beans;

	public DefaultBeanFactory() {
		beans = new HashMap<String, Object>();
	}

	public void putInCache(Object bean) {
		if (bean != null)
			this.beans.put(bean.getClass().getName(), bean);
	}

	public <T> T getBeanFromCache(Class<T> clazz) {
		T bean = null;
		try {
			bean = (T) this.beans.get(clazz.getName());
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return bean;
	}

	public <T> T getBeanFromCache(String className) {
		T bean = (T) this.beans.get(className);
		return bean;
	}

	@Override
	public <T> T getBean(Class<T> clazz, boolean singleton) {
		return this.getBean(clazz, singleton, null);
	}

	@Override
	public <T> T getBean(Class<T> clazz, boolean singleton, AfterConstruction after) {
		T bean = null;
		if (!singleton) {
			bean = buildBean(clazz, after);
			return bean;
		}

		bean = getBeanFromCache(clazz);
		if (bean == null) {
			bean = buildBean(clazz, after);
			putInCache(bean);
		}

		return bean;
	}

	@Override
	public Object getBean(String className, boolean singleton, AfterConstruction after) {
		Object bean = null;
		if (!singleton) {
			bean = buildBean(className, after);
			return bean;
		}

		bean = getBeanFromCache(className);
		if (bean == null) {
			bean = buildBean(className, after);
			putInCache(bean);
		}

		return bean;
	}

	public <T> T buildBean(String className, AfterConstruction after) {
		return (T) buildBean(ReflectUtils.loadClass(className), after);
	}

	public <T> T buildBean(Class<T> clazz, AfterConstruction after) {
		T bean = ReflectUtils.newInstance(clazz);
		if (after != null)
			after.afterConstruction(bean);
		awareBeanFactory(bean);
		initBean(bean);
		return bean;
	}

	protected <T> void awareBeanFactory(T bean) {
		if (bean instanceof BeanFactoryAware) {
			BeanFactoryAware awareBean = (BeanFactoryAware) bean;
			awareBean.setBeanFactory(this);
		}
	}

	protected <T> void initBean(T bean) {
		if (bean instanceof InitializeBean) {
			InitializeBean initBean = (InitializeBean) bean;
			initBean.init();
		}
	}

}
