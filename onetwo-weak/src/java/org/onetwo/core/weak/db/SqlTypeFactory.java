package org.onetwo.core.weak.db;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Types;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public final class SqlTypeFactory {
	
	private static final Map<String, Integer> BASIC_TYPES;
	
	static {
		Map<String, Integer> basicTypes = new HashMap<String, Integer>();
		basicTypes.put(int.class.getName(), Types.INTEGER);
		basicTypes.put(long.class.getName(), Types.BIGINT);
		basicTypes.put(short.class.getName(), Types.SMALLINT);
		basicTypes.put(byte.class.getName(), Types.TINYINT);
		basicTypes.put(float.class.getName(), Types.FLOAT);
		basicTypes.put(double.class.getName(), Types.DOUBLE);
		basicTypes.put(boolean.class.getName(), Types.BOOLEAN);
		
		basicTypes.put(Integer.class.getName(), Types.INTEGER);
		basicTypes.put(Long.class.getName(), Types.BIGINT);
		basicTypes.put(Short.class.getName(), Types.SMALLINT);
		basicTypes.put(Byte.class.getName(), Types.TINYINT);
		basicTypes.put(Float.class.getName(), Types.FLOAT);
		basicTypes.put(Double.class.getName(), Types.DOUBLE);
		basicTypes.put(Boolean.class.getName(), Types.BOOLEAN);
		
		basicTypes.put(String.class.getName(), Types.VARCHAR);
		basicTypes.put(BigDecimal.class.getName(), Types.NUMERIC);
		basicTypes.put(BigInteger.class.getName(), Types.NUMERIC);
		basicTypes.put(Number.class.getName(), Types.NUMERIC);
		basicTypes.put(java.util.Date.class.getName(), Types.TIMESTAMP);
		basicTypes.put(java.util.Calendar.class.getName(), Types.TIMESTAMP);
		basicTypes.put(java.sql.Date.class.getName(), Types.DATE);
		basicTypes.put(java.sql.Time.class.getName(), Types.TIME);
		basicTypes.put(java.sql.Timestamp.class.getName(), Types.TIMESTAMP);
		basicTypes.put(byte[].class.getName(), Types.BINARY);
		
		BASIC_TYPES = Collections.unmodifiableMap(basicTypes);
	}
	
	public static int getType(String name){
		Integer type = BASIC_TYPES.get(name);
		if(type==null){
			type = new Integer(DBUtils.TYPE_UNKNOW);
		}
		return type.intValue();
	}
	
	public static int getType(Class cls){
		return getType(cls.getName());
	}
	
	public static int getType(Object value){
		if(value==null){
			return DBUtils.TYPE_UNKNOW;
		}
		return getType(value.getClass());
	}
}
;