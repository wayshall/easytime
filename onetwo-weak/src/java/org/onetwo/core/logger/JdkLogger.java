package org.onetwo.core.logger;

import java.util.logging.Level;

public class JdkLogger extends Logger{
	
	private java.util.logging.Logger logger;
	
	public JdkLogger(java.util.logging.Logger logger){
		this.logger = logger;
	}

	@Override
	public void error(String msg, Throwable e) {
		this.logger.log(Level.SEVERE, msg, e);
	}

	@Override
	public void error(String msg) {
		this.logger.log(Level.SEVERE, msg);
	}

	@Override
	public void info(String msg, Throwable e) {
		this.logger.log(Level.INFO, msg, e);
	}

	@Override
	public void info(String msg) {
		this.logger.info(msg);
	}

	@Override
	public void warn(String msg, Throwable e) {
		this.logger.log(Level.WARNING, msg, e);
	}

	@Override
	public void warn(String msg) {
		this.logger.warning(msg);
	}

}
