package org.onetwo.ext.security.config;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.onetwo.ext.security.AnnotationUtils;

public class ConfigManager{
	public static final String NAME = "authenticConfig";
	
//	protected Logger logger = Logger.getLogger(ConfigManager.class);

	private Map<String, AuthenticConfig> configs = new HashMap<String, AuthenticConfig>();
	
	private boolean devMode ;

	public AuthenticConfig getConfig(Class<?> clazz, String methodName) {
		return this.getConfig(clazz, AnnotationUtils.findMethod(clazz, methodName));
	}

	public AuthenticConfig getConfig(Class<?> clazz, Method method) {
		String configName = method.toGenericString();
		AuthenticConfig conf = null;
		if(!devMode)
			conf = configs.get(configName);
		if (conf == null) {
			conf = readConfig(clazz, method);
		}
		return conf;
	}

	protected AuthenticConfig readConfig(Class<?> clazz, Method method) {
		ConfigBuilder configBuilder = getConfigBuilder(clazz, method);
		AuthenticConfig config = configBuilder.buildAuthenConfig();
		configs.put(config.getName(), config);
		return config;
	}
	
	protected ConfigBuilder getConfigBuilder(Class<?> clazz, Method method){
		return new ConfigBuilder(clazz, method);
	}
	
    public void setDevMode(String mode) {
        this.devMode = "true".equals(mode);
    }

}
