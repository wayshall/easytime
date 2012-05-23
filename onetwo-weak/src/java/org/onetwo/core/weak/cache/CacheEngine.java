package org.onetwo.core.weak.cache;

public interface CacheEngine {
	
	public void init();
	
	public void clear();
	
	public void add(String fullKey, Object value);
	
	public void add(String fullKey, String key, Object value);
	
	public Object get(String fullKey);
	
	public Object get(String fullKey, String key);
	
	public Object remove(String fullKey);
	
	public Object remove(String fullKey, String key);

}
