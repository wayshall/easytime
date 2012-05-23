package org.onetwo.core.logger;


@SuppressWarnings("unchecked")
public class JdkLoggerFactory extends LoggerFactory {

	@Override
	public Logger getLogger(Class clazz) {
		java.util.logging.Logger logger = java.util.logging.Logger.getLogger(clazz.getName());
		JdkLogger jdkLogger = new JdkLogger(logger);
		return jdkLogger;
	}

}
