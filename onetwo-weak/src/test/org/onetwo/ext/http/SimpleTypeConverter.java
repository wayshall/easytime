package org.onetwo.ext.http;

import java.util.Date;
import java.util.Map;

import ognl.DefaultTypeConverter;
import ognl.OgnlOps;

import org.onetwo.core.util.DateUtil;

public class SimpleTypeConverter extends DefaultTypeConverter {

	public static final String DATE_FORMAT = "EEE MMM d HH:mm:ss z yyyy";

	public Object convertValue(Map context, Object value, Class toType) {
        Object  result = null;
		if (value instanceof String && toType==Date.class) {
			result = DateUtil.parseStrToDateByPattern(value.toString(), DATE_FORMAT, false);
		}else
			result = OgnlOps.convertValue(value, toType);
		return result;
	}
}
