package org.onetwo.core.logger;

import java.util.logging.Logger;

public class Test {

	public static void main(String[] args){
		Logger logger = Logger.getLogger(Test.class.getName());
		logger.info("test");
	}
}
