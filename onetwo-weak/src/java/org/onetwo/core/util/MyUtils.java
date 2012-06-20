package org.onetwo.core.util;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import ognl.Ognl;
import ognl.OgnlException;

import org.onetwo.core.exception.BaseException;
import org.onetwo.core.exception.ServiceException;

@SuppressWarnings("unchecked")
public class MyUtils {
	public static final char CHOMP = '\r';
	public static final char LINE = '\n';
	public static final char SPACE = ' ';
	public static final String HTML_BLANK = "&nbsp;";
	public static final String HTML_NEWLINE = "<br/>";
	public static final String REGEX = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	public static final Pattern USERNAME_PATTER = Pattern.compile(REGEX);

	private MyUtils() {
	}


	public static <T> T simpleConvert(Object val, Class<T> toType){
		return simpleConvert(val, toType, null);
	}

	public static <T> T simpleConvert(Object val, Class<T> toType, T def){
		if(val==null)
			return def;
		Object newValue = null;
		if(toType==null)
			toType = (Class<T>)String.class;
		
		if(toType.isAssignableFrom(val.getClass()))
			return (T)val;
		
		if(val.getClass().isArray()){
			if(Array.getLength(val)==0)
				return def;
			val = Array.get(val, 0);
			if(val==null)
				return def;
			return simpleConvert(val, toType, def);
		}
		if(toType==Long.class || toType==long.class){
			newValue = NumberUtils.toLong((String)val);
		}else if(toType==Integer.class || toType==int.class){
			newValue = NumberUtils.toInt((String)val);
		}else if(toType==Double.class || toType==double.class){
			newValue = NumberUtils.toDouble((String)val);
		}else if(toType==Float.class || toType==float.class){
			newValue = NumberUtils.toFloat((String)val);
		}else if(toType==Boolean.class || toType==boolean.class){
			newValue = BooleanUtils.toBooleanObject((String)val);
		}else if(toType==String.class){
			newValue = val.toString();
		}else{
			newValue = val;
			if(val==null)
				newValue = def;
		}
		return (T)newValue;
	}
	
	public static Throwable getCauseServiceException(Throwable e){
		Throwable se ;
		if(!(e instanceof BaseException) && e.getCause()!=null){
			se = getCauseServiceException(e.getCause());
		}else{
			se = e;
		}
		return se;
	}

	public final static String htmlEncode(String s) {
		if (StringUtils.isBlank(s))
			return "";

		StringBuilder str = new StringBuilder();

		for (int j = 0; j < s.length(); j++) {
			char c = s.charAt(j);

			// encode standard ASCII characters into HTML entities where needed
			if (c < '\200') {
				switch (c) {
				case '"':
					str.append("&quot;");

					break;

				case '&':
					str.append("&amp;");

					break;

				case '<':
					str.append("&lt;");

					break;

				case '>':
					str.append("&gt;");

					break;

				case '\n':
					str.append("<BR/>");

					/*
					 * case ' ':
					 * if(Locale.CHINA.getLanguage().equals(StrutsUtils.getCurrentSessionLocale().getLanguage()))
					 * str.append(" "); else str.append(" "); break;
					 */

				default:
					str.append(c);
				}
			} else if (c < '\377') {
				String hexChars = "0123456789ABCDEF";
				int a = c % 16;
				int b = (c - a) / 16;
				String hex = "" + hexChars.charAt(b) + hexChars.charAt(a);
				str.append("&#x" + hex + ";");
			} else {
				str.append(c);
			}
		}

		return str.toString();
	}

	public static Map<Object, Object> convertParamMap(Object... params) {
		if(params==null || params.length==0)
			return null;
		Map<Object, Object> properties = null;
		if (params.length % 2 == 1)
			throw new IllegalArgumentException("参数个数必须是偶数个！");

		properties = new HashMap<Object, Object>();
		int index = 0;
		Object name = null;
		for (Object s : params) {
			if (index % 2 == 0) {
				if (s == null || StringUtils.isBlank(s.toString()))
					throw new IllegalArgumentException("字段名称不能为空！");
				name = s;
			} else {
				properties.put(name, s);
			}
			index++;
		}
		return properties;
	}

	public static String getLikeString(String str) {
		if (str.startsWith("%") || str.endsWith("%"))
			return str;
		StringBuilder sb = new StringBuilder();
		return sb.append("%").append(str).append("%").toString();
	}


	public static Serializable getEntityProperty(Object entity, String propName) {
		Assert.hasLength(propName);
		Serializable value = null;
		try {
			value = (Serializable) Ognl.getValue(propName, entity);
		} catch (Exception e) {
			throw new ServiceException("get the entity[" + entity + "] property error property["+propName+"].", e);
		}
		return value;
	}

	public static void setEntityProperty(Object entity, String propName, Object value) {
		Assert.hasLength(propName);
		try {
			Ognl.setValue(propName, entity, value);
		} catch (OgnlException e) {
			throw new ServiceException("set the entity[" + entity + "] property["+propName+"] . ", e);
		}
	}


	public static void deleteFile(String path) {
		if (StringUtils.isBlank(path))
			return;
		File file = new File(path);
		file.delete();
	}



	public static String appendPathSeparator(String path) {
		if (!path.endsWith(String.valueOf(File.separatorChar)))
			path += File.separatorChar;
		return path;
	}

	public static String append(String... strings) {
		if (strings == null || strings.length == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		for (String str : strings)
			if (StringUtils.isNotBlank(str))
				sb.append(str);
		return sb.toString();
	}

	public static boolean isAllowUserName(String userName) {
		return USERNAME_PATTER.matcher(userName).matches();
	}

	public static List asList(Object array) {
		return asList(array, true);
	}

	public static List asList(Object array, boolean trimNull) {
		if (array == null)
			return null;
		List list = null;
		if (array instanceof Collection) {
			if (trimNull)
				list = stripNull((Collection) array);
			else {// 本else块由mrs添加
				list = new ArrayList();
				list.addAll((Collection) array);
			}
		} else if (array.getClass().isArray()) {
			int length = Array.getLength(array);
			list = new ArrayList();
			for (int i = 0; i < length; i++) {
				Object value = Array.get(array, i);
				if (value == null || (value instanceof String && StringUtils.isBlank(value.toString())))
					continue;
				list.add(value);
			}
		} else {
			list = new ArrayList();
			list.add(array);
		}
		return (list == null || list.isEmpty()) ? null : list;
	}

	public static int getSize(Object array) {
		int size = 0;
		if (array == null)
			return size;
		if (array instanceof Collection) {
			size = ((Collection) array).size();
		} else if (array.getClass().isArray()) {
			size = Array.getLength(array);
		} else if (array instanceof Map) {
			size = ((Map) array).size();
		}
		return size;
	}

	public static boolean isArray(Object value) {
		return value != null && (value instanceof Collection || value.getClass().isArray());
	}

	public static List stripNull(Collection collection) {
		if (collection == null || collection.size() < 1)
			return null;
		List list = null;
		for (Object obj : collection) {
			if (obj == null || (obj instanceof String && StringUtils.isBlank(obj.toString())))
				continue;
			if (list == null)
				list = new ArrayList();
			list.add(obj);
		}
		return list;
	}

	public static List array2List(Object[] array) {
		if (array == null || array.length < 1)
			return null;
		List list = new ArrayList();
		for (Object obj : array) {
			if (obj == null || (obj instanceof String && StringUtils.isBlank(obj.toString())))
				continue;
			list.add(obj);
		}
		return list;
	}


	public static String scaleRegionCode(String regionCode) {
		return scaleRegionCode(regionCode, true);
	}

	/**
	 * 解析给定地区编码，将此编码的以零标识的下级地区编码占位去除
	 * 
	 * @param districtCode
	 * @return 当编码非法或为空时反回“%”、反回去除占位0后加上“%”的编码
	 * @author Marisheng
	 */
	public static String scaleRegionCode(String regionCode, Boolean nullAble) {
		// 正确的编码规则为长度等于十六，每四位一个级别的数字编码方式
		if (regionCode == null || regionCode.length() != 16) {
			return (nullAble ? null : "%");
		}
		String sqlCode;
		if (regionCode.endsWith("000000000000")) {
			// 国家级代码
			sqlCode = regionCode.substring(0, 4);
		} else if (regionCode.endsWith("00000000")) {
			// 省级
			sqlCode = regionCode.substring(0, 8);
		} else if (regionCode.endsWith("0000")) {
			// 市级
			sqlCode = regionCode.substring(0, 12);
		} else {
			// 区级
			sqlCode = regionCode;
		}

		return sqlCode + "%";
	}

	/**
	 * 解析给定行业编码，将此编码加以“%”结尾
	 * 
	 * @param tradeCode
	 * @return 编码为空时反回“%”、加以“%”结尾编码
	 * @author Marisheng
	 */
	public static String scaleIndustryCode(String industryCode, Boolean nullAble) {
		if (industryCode == null || industryCode.length() == 0) {
			return (nullAble ? null : "%");
		}
		return industryCode + "%";
	}

	public static String scaleIndustryCode(String industryCode) {
		return scaleIndustryCode(industryCode, true);
	}

	public static String nounderLineName(String str, boolean isFirstUpper) {
		char[] chars = str.toCharArray();
		StringBuilder newStr = new StringBuilder();
		boolean needUpper = isFirstUpper;
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (needUpper) {
				c = Character.toUpperCase(c);
				needUpper = false;
			}
			if (c == '_') {
				needUpper = true;
				continue;
			}
			newStr.append(c);
		}
		return newStr.toString();
	}

	public static String underLineName(String name) {
		StringBuffer table = new StringBuffer();
		char[] chars = name.toCharArray();
		table.append(Character.toLowerCase(chars[0]));
		for (int i = 1; i < chars.length; i++) {
			char ch = chars[i];
			if (Character.isUpperCase(ch)) {
				table.append("_");
				ch = Character.toLowerCase(ch);
			}
			table.append(ch);
		}
		return table.toString();
	}
	
	public static String getCountSql(String sql){
		String hql = sql;
		if(hql.indexOf("group by")!=-1){
			hql = "select count(*) from (" + hql + ") ";
		}else{
			hql = StringUtils.substringAfter(hql, "from ");
			hql = StringUtils.substringBefore(hql, " order by ");
			hql = "select count(*) from " + hql;
		}
		return hql;
	}
	
	public static Map toMap(List<? extends Map> datas, String keyName, String valueName){
		if(datas==null)
			return null;
		Map map = new NonCaseMap();
		for(Map row : datas){
			map.put(row.get(keyName), row.get(valueName));
		}
		return map;
	}

	public static void main(String[] args) {
		String name = "aaaBb";
		System.out.println(underLineName(name));
		char a = '\n';
		System.out.print((int) a);
		System.out.print("test");
	}
}
