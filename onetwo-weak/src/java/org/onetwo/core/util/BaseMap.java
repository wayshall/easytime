package org.onetwo.core.util;

import java.util.HashMap;

@SuppressWarnings("serial")
public class BaseMap<K, V> extends HashMap<K, V> {

	public Object get(Object key, Object def){
		Object val = get(key);
		if(val==null)
			return def;
		return val;
	}
	
	public String getString(Object key){
		return getString(key, null);
	}
	
	public String getString(Object key, String def){
		Object val = get(key, def);
		return convert(val, String.class, def);
	}
	
	
	public Long getLong(String key){
		return this.getLong(key, null);
	}
	
	public Long getLong(String key, Long def){
		Object val = get(key);
		return convert(val, Long.class, def);
	}
	
	public Double getDouble(String key, Double def){
		Object val = get(key);
		return convert(val, Double.class, def);
	}
	
	public Float getFloat(String key, Float def){
		Object val = get(key);
		return convert(val, Float.class, def);
	}
	
	protected <T> T convert(Object val, Class<T> toType, T def){
		return MyUtils.simpleConvert(val, toType, def);
	}
	
	public Integer getInteger(String key){
		return getInteger(key, null);
	}
	
	public Integer getInteger(String key, Integer def){
		Object val = get(key);
		return convert(val, Integer.class, def);
	}
}
