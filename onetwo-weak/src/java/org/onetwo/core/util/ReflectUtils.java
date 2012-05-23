package org.onetwo.core.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.onetwo.core.exception.BaseException;
import org.onetwo.core.logger.Logger;

@SuppressWarnings("unchecked")
public class ReflectUtils {
	public static final Logger logger = Logger.getLogger(ReflectUtils.class);

	private ReflectUtils() {
	}

	public static Field findField(Class<?> clazz, String fieldName) {
		if (clazz == null || clazz == Object.class)
			return null;
		Field field = null;
		try {
			field = clazz.getDeclaredField(fieldName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (field == null) {
			field = findField(clazz.getSuperclass(), fieldName);
		}

		return field;
	}

	public static PropertyDescriptor findProperty(Class<?> clazz, String propName) {
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(clazz, Object.class);
		} catch (Exception e) {
			throw new BaseException(e);
		}
		for (PropertyDescriptor prop : beanInfo.getPropertyDescriptors()) {
			if (propName.equals(prop.getName()))
				return prop;
		}
		return null;
	}

	public static PropertyDescriptor[] desribProperties(Class<?> clazz) {
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(clazz, Object.class);
		} catch (Exception e) {
			throw new BaseException(e);
		}
		return beanInfo.getPropertyDescriptors();
	}

	public static Method findPublicMethod(Class<?> clazz, String methodName) {
		Method[] publicMethods = clazz.getMethods();
		if (publicMethods == null || publicMethods.length == 0)
			return null;
		for (Method method : publicMethods) {
			if (method.getName().equals(methodName))
				return method;
		}
		return null;
	}

	/** ****************************** */

	public static Object invokePublicMethod(Object bean, String methodName, Object...objects){
		Object value = null;
		try {
			Class[] clazz = null;
			if(objects==null){
				objects = new Object[]{};
				clazz = new Class[]{};
			}else{
				List<Class> list = new ArrayList<Class>();
				for(Object obj : objects){
					list.add(obj.getClass());
				}
				clazz = list.toArray(new Class[list.size()]);
			}
			
			Method method = null;
			method = bean.getClass().getMethod(methodName, clazz);
			method.setAccessible(true);
			
			value = invokePublicMethod(bean, method, objects);
		} catch (Exception e) {
			throw new BaseException("invokeMethod error!", e);
		}
		return value;

	}
	
	public static Object invokePublicMethod(Object bean, Method method, Object...objects){
		Object value = null;
		try {
			if(objects==null)
				objects = new Object[]{};
			
			method.setAccessible(true);
			value = method.invoke(bean, objects);
		} catch (Exception e) {
			throw new BaseException("invokeMethod error!", e);
		}
		return value;
	}


	public static Method getMethod(Object bean, String methodName, Class... argsClass){
		Method method = null;
		try {
			method = bean.getClass().getMethod(methodName, argsClass);
		} catch (Exception e) {
			throw new BaseException("cant not get method : " + methodName, e);
		} 
		return method;
	}

	public static Method[] getPrefixMethod(Object bean, String prefix) {
		Method[] methods = bean.getClass().getMethods();
		if (prefix == null || prefix.trim().length() < 1)
			return methods;
		List<Method> list = new ArrayList<Method>();
		for (Method method : methods) {
			if (method.getName().startsWith(prefix)) {
				list.add(method);
			}
		}
		if (list == null || list.isEmpty()) {
			return null;
		} else {
			return (Method[]) list.toArray();
		}
	}

	public static <T extends Annotation> Method[] getMethodByAnnotation(Object bean, Class<T> annotationClass) {
		List<Method> annoMethods = new ArrayList<Method>();
		Class clz = bean.getClass();
		Method[] methods = clz.getMethods();
		for (Method method : methods) {
			Annotation anno = method.getAnnotation(annotationClass);
			if (anno != null) {
				annoMethods.add(method);
			}
		}
		if (annoMethods == null || annoMethods.isEmpty()) {
			return null;
		} else {
			return annoMethods.toArray(new Method[annoMethods.size()]);
		}
	}

	public static <T extends Annotation> Field[] getFieldByAnnotation(Object bean, Class<T> annotationClass) {
		List<Field> fields = new ArrayList<Field>();
		Class clz = bean.getClass();
		Field[] allFields = clz.getDeclaredFields();
		for (Field f : allFields) {
			Annotation anno = f.getAnnotation(annotationClass);
			if (anno != null) {
				fields.add(f);
			}
		}
		if (fields == null || fields.isEmpty()) {
			return null;
		} else {
			return fields.toArray(new Field[fields.size()]);
		}
	}

	public static Class<?> loadClass(String className) {
		Class<?> clz = null;
		try {
			clz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new BaseException("class not found : "+className, e);
		}
		return clz;
	}

	public static Object newInstance(String className) {
		return newInstance(loadClass(className));
	}

	public static <T> T newInstance(Class<T> clazz) {
		T instance = null;
		try {
			instance = clazz.newInstance();
		} catch (Exception e) {
			throw new BaseException("instantce class error : " + clazz, e);
		}
		return instance;
	}

	public static <T> T newInstance(Class<T> clazz, Object...objects) {
		T instance = null;
		try {
			List<Class> paramTypes = new ArrayList();
			for(Object obj : objects){
				paramTypes.add(obj.getClass());
			}
			Constructor<T> construct = clazz.getConstructor(paramTypes.toArray(new Class[paramTypes.size()]));
			instance = construct.newInstance(objects);
		} catch (Exception e) {
			throw new BaseException("instantce class error : " + clazz, e);
		}
		return instance;
	}
	
	public static List<Field> getFields(Class clazz){
		return getFields(clazz, Object.class);
	}
	
	public static List<Field> getFields(Class clazz, Class parent){
		Field[] fs = clazz.getDeclaredFields();
		if(fs==null)
			return null;

		List<Field> fields = new ArrayList<Field>();
		for(Field f : fs)
			fields.add(f);
		
		Class p = clazz.getSuperclass();
		if(p==null || p.equals(parent))
			return fields;
		
		List pfs = getFields(p, parent);
		if(pfs==null || pfs.isEmpty())
			return fields;
		
		fields.addAll(pfs);
		return fields;
	}
}
