package org.onetwo.core.weak.cache;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class DefaultCacheEngine implements CacheEngine{
	
	public Map<String, Object> cache;

	@Override
	public void add(String fullKey, Object value) {
		cache.put(fullKey, value);
	}

	@Override
	public void add(String fullKey, String key, Object value) {
		Map m = (Map) cache.get(fullKey);
		if(m==null)
			m = new HashMap();
		m.put(key, value);
		cache.put(fullKey, m);
	}

	@Override
	public Object get(String fullKey, String key) {
		Object v = null;
		Map m = (Map) get(fullKey);
		if(m!=null)
			v = m.get(key);
		return v;
	}

	@Override
	public Object get(String fullKey) {
		return cache.get(fullKey);
	}

	@Override
	public Object remove(String fullKey, String key) {
		Object v = null;
		Map m = (Map) cache.get(fullKey);
		if(m!=null)
			v = m.remove(key);
		return v;
	}

	@Override
	public Object remove(String fullKey) {
		return cache.remove(fullKey);
	}

	@Override
	public void init() {
		cache = new HashMap();
	}

	@Override
	public void clear() {
		cache.clear();
	}

}
