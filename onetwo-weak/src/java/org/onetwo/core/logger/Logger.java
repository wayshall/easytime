package org.onetwo.core.logger;


@SuppressWarnings("unchecked")
abstract public class Logger {
	
	private static LoggerFactory facotry = new JdkLoggerFactory();
	
	public static Logger getLogger(Class clazz){
		return facotry.getLogger(clazz);
	}

	abstract public void info(String msg);
	
	abstract public void info(String msg, Throwable e);

	abstract public void warn(String msg);
	
	abstract public void warn(String msg, Throwable e);

	abstract public void error(String msg);
	
	abstract public void error(String msg, Throwable e);
}
