package org.onetwo.core.codegenerator.db;

import java.sql.Types;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({ "unused", "unchecked" })
public class TypeMapper {
	
	private static final Map<Integer, Class> MAPPING ;
	
	static{
		
		Map<Integer, Class> types = new HashMap<Integer, Class>();
		types.put(Types.INTEGER, Integer.class);
		types.put(Types.BIGINT, Long.class);
		types.put(Types.DECIMAL, Long.class);
		types.put(Types.VARCHAR, String.class);
		types.put(Types.CLOB, String.class);
		types.put(Types.FLOAT, Float.class);
		types.put(Types.TINYINT, Short.class);
		types.put(Types.DATE, Date.class); 
		types.put(Types.TIME, Date.class);
		types.put(Types.TIMESTAMP, Date.class);
		
		MAPPING = Collections.unmodifiableMap(types);
	}
	
	public static Class getJavaType(int sqlType){
		Class clz = MAPPING.get(sqlType);
		if(clz==null)
			return String.class;
		return clz;
	}

}
