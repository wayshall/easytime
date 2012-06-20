package org.onetwo.ext.http;

import ognl.OgnlContext;
import ognl.TypeConverter;

abstract public class ConstantUtils {
	
	public static final TypeConverter TYPE_CONVERT = new SimpleTypeConverter();
	
	public static OgnlContext getOgnlContext(){
		OgnlContext context = new OgnlContext();
		context.setTypeConverter(TYPE_CONVERT);
		return context;
	}

}
