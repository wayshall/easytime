package org.onetwo.core.logger;

import org.onetwo.core.util.ReflectUtils;
import org.onetwo.core.util.StringUtils;

@SuppressWarnings("unchecked")
abstract public class LoggerFactory {
	
	public static final String LOGGER_FACTORY = "weak.logger.factory";
	
	private static LoggerFactory instance;
	
	static {
		String factoryClass = System.getProperty(LOGGER_FACTORY);
		if(StringUtils.isNotBlank(factoryClass))
			instance = (LoggerFactory) ReflectUtils.newInstance(factoryClass);
		else
			instance = new JdkLoggerFactory();
	}
	
	public static LoggerFactory getInstance(){
		return instance;
	}
	
	abstract public Logger getLogger(Class clazz);

}
