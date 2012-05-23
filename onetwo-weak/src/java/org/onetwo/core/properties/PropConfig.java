package org.onetwo.core.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;
import org.onetwo.core.exception.BaseException;
import org.onetwo.core.util.FileUtils;
import org.onetwo.core.util.MyUtils;
import org.onetwo.core.util.ReflectUtils;
import org.onetwo.core.util.SimpleExpression;
import org.onetwo.core.util.StringUtils;
import org.onetwo.core.util.ValueProvider;

@SuppressWarnings("unchecked")
public class PropConfig implements VariableSupporter {
	
	public static final String CONFIG_KEY = "config";

	protected Properties config;

	protected File file;

	protected VariableExpositor expositor;
	
	protected SimpleExpression se = new SimpleExpression("#{", "}");
	
	private Map cache = new HashMap();

	public PropConfig(String configName) {
		this(configName, true);
	}

	public PropConfig(String configName, boolean cacheable) {
		config = new Properties();
		this.parseResource(configName);
		this.loadConfig(cacheable);
	}

	public PropConfig(File configFile, boolean cacheable) {
		config = new Properties();
		this.file = configFile;
		this.loadConfig(cacheable);
	}

	public void saveToFile() {
		try {
			FileOutputStream fout = new FileOutputStream(file);
			config.store(fout, null);
			System.out.println("file : " + file.getPath());
			fout.close();
		} catch (Exception e) {
			throw new BaseException("save config error!", e);
		}
	}

	protected void parseResource(String configName) {
		try {
			if (!configName.endsWith(".properties"))
				configName += ".properties";
			file = new File(configName);
			if (!file.exists()) {
				// URL path = PropConfig.class.getResource(configName);
				if (configName.indexOf(':') == -1) {
					configName = FileUtils.getResourcePath(configName);
				}
				file = new File(configName);
				if (!file.exists())
					throw new FileNotFoundException("path :" + file.getPath());
			}
		} catch (Exception e) {
			throw new BaseException("load property config error: " + configName, e);
		}
	}

	public void loadConfig(boolean cacheable) {
		FileInputStream fin = null;
		try {
			clear();
			fin = new FileInputStream(file);
			config.load(fin);
			fin.close();
			System.out.println("loaded the property config: " + file.getPath());
		} catch (Exception e) {
			throw new RuntimeException("load config error!", e);
		} finally {
			if (fin != null)
				try {
					fin.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
		}
		expositor = new VariableExpositor(this, cacheable);
	}
	
	protected void putInCache(String key, Object value){
		this.cache.put(key, value);
	}
	
	protected Object getFromCache(String key){
		return cache.get(key);
	}

	public String getProperty(String key, String defaultValue) {
		String value = this.getVariable(key);
		return value == null ? defaultValue : value;
	}

	public Enumeration<String> keys() {
		return (Enumeration<String>) config.propertyNames();
	}

	public String getVariable(String key) {
		String value = config.getProperty(key);
		//
		if (value != null) {
			value = this.expositor.explainVariable(value);
		}
		return value;
	}

	public String getVariable(String key, Object...objects) {
		String value = config.getProperty(key);
		//
		if (value != null) {
			value = this.expositor.explainVariable(value);
		}
		if(se.isExpresstion(value)){
			final Map context = MyUtils.convertParamMap(objects);
			value = se.parse(value, new ValueProvider(){

				@Override
				public String findString(String var) {
					Object v = context.get(var);
					if(v!=null)
						return v.toString();
					return "";
				}
				
			});
		}
		return value;
	}

	public String getProperty(String key) {
		return this.getVariable(key);
	}

	public Object setProperty(String key, String value) {
		return config.setProperty(key, value);
	}

	public Integer getInteger(String key, Integer def) {
		String value = this.getProperty(key);
		if (StringUtils.isBlank(value)) {
			return def;
		}
		Integer integer = null;
		try {
			integer = new Integer(value);
		} catch (Exception e) {
			integer = def;
		}
		return integer;
	}

	public Integer getInteger(String key) {
		return getInteger(key, new Integer(0));
	}

	public Class getClass(String key) {
		return this.getClass(key, null);
	}

	public Class getClass(String key, Class defClass) {
		//cache
		Class clazz = (Class) getFromCache(key);
		if(clazz!=null)
			return clazz;
		
		String className = this.getVariable(key);
		
		try {
			if(StringUtils.isBlank(className))
				clazz = defClass;
			else
				clazz = ReflectUtils.loadClass(className);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (clazz == null && defClass != null)
			clazz = defClass;
		
		this.putInCache(key, clazz);
		
		return clazz;
	}

	public List<Class> getClassList(String key) {
		//cache
		List<Class> classes = (List<Class>) getFromCache(key);
		if(classes!=null)
			return classes;
		
		String value = this.getVariable(key);
		
		if(StringUtils.isBlank(value))
			return null;
		String[] valueses = value.split(",");
		if(valueses==null || valueses.length<1)
			return null;
		
		classes = new ArrayList<Class>();
		for(String v : valueses){
			if(StringUtils.isBlank(v))
				continue;
			classes.add(ReflectUtils.loadClass(v.trim()));
		}
		
		this.putInCache(key, classes);
		
		return classes; 
	}

	public JSONArray getJSONArray(String key) {
		String json = this.getVariable(key);

		//cache
		JSONArray result = (JSONArray) getFromCache(key);
		if(result!=null)
			return result;
		
		try {
			result = new JSONArray(json);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException(e);
		}
		
		putInCache(key, result);
		
		return result;
	}

	public JSONObject getJSONObject(String key) {
		String json = this.getVariable(key);

		//cache
		JSONObject result = (JSONObject) getFromCache(key);
		if(result!=null)
			return result;
		
		try {
			result = new JSONObject(json);
		} catch (Exception e) {
			throw new BaseException(e);
		}
		
		putInCache(key, result);
		
		return result;
	}

	public Long getLong(String key, Long def) {
		String value = this.getProperty(key);
		if (StringUtils.isBlank(value)) {
			return def;
		}
		Long longValue = null;
		try {
			longValue = new Long(value);
		} catch (Exception e) {
			longValue = def;
		}
		return longValue;
	}

	public Boolean getBoolean(String key) {
		String value = this.getProperty(key);
		if (StringUtils.isBlank(value))
			return false;
		Boolean booleanValue = false;
		try {
			booleanValue = Boolean.parseBoolean(value);
		} catch (Exception e) {
			booleanValue = false;
		}
		return booleanValue;
	}

	public File getFile() {
		return file;
	}

	public Enumeration<String> propertyNames() {
		return (Enumeration<String>) config.propertyNames();
	}

	public Properties getConfig() {
		Properties p = (Properties) getFromCache(CONFIG_KEY);
		if(p!=null)
			return p;
		
		p = new Properties();
		Enumeration<String> en = this.propertyNames();
		while (en.hasMoreElements()) {
			String key = en.nextElement();
			p.put(key, getVariable(key));
		}
		
		putInCache(CONFIG_KEY, p);
		
		return p;
	}

	public void clear() {
		if(config!=null)
			this.config.clear();
		if(expositor!=null)
			this.expositor.clear();
		if(cache!=null)
			this.cache.clear();
	}

	public static void main(String[] args) throws Exception {
		PropConfig p = new PropConfig("/conf/mailConfig.properties");
		System.out.println(p.getProperty("host.name"));
		p.setProperty("to.name", "asdfasdf");
		System.out.println(p.getProperty("to.name"));
		p.saveToFile();
	}
}
