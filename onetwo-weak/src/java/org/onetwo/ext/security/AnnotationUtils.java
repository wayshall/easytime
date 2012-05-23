package org.onetwo.ext.security;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import org.onetwo.core.exception.ServiceException;

public class AnnotationUtils {

	private AnnotationUtils() {
	}

	public static <T extends Annotation> T findAnnotation(Class<?> clazz, Class<T> annotationClass) {
		T annotation = clazz.getAnnotation(annotationClass);
		return annotation;
	}

	public static <T extends Annotation> T findAnnotation(Class<?> clazz, String methodName, Class<T> annotationClass, Set<Class> stopClass) {
		Method method = null;
		try {
			method = clazz.getMethod(methodName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return findAnnotation(clazz, method, annotationClass, stopClass);
	}

	public static <T extends Annotation> T findAnnotation(Class<?> clazz, Method method, Class<T> annotationClass, Set<Class> stopClass) {
		T annotation = null;
		if (method != null)
			annotation = method.getAnnotation(annotationClass);
		if (annotation == null)
			annotation = clazz.getAnnotation(annotationClass);
		if (annotation == null) {
			Class<?> parent = clazz.getSuperclass();
			if (parent == null || parent == Object.class || (stopClass!=null && stopClass.contains(parent)))
				return null;

			Method parentMethod = null;
			if (method != null)
				parentMethod = findMethod(parent, method.getName(), method.getParameterTypes());
			return findAnnotation(parent, parentMethod, annotationClass, stopClass);
		}
		return annotation;
	}

	public static <T extends Annotation> T findFieldAnnotation(Class<?> clazz, String fieldName, Class<T> annotationClass) {
		try {
			Field field = findField(clazz, fieldName);
			return findFieldAnnotation(clazz, field, annotationClass);
		} catch (Exception e) {
			return null;
		}
	}

	public static Field findField(Class<?> clazz, String fieldName) {
		Field field = null;
		try {
			if (field == null)
				field = clazz.getDeclaredField(fieldName);
		} catch (Exception e) {
		}
		
		try {
			field = clazz.getField(fieldName);
		} catch (Exception e) {
		}
		
		if(field==null)
			throw new ServiceException("找不到字段：" + fieldName);
		
		return field;
	}

	public static <T extends Annotation> T findFieldAnnotation(Class<?> clazz, Field field, Class<T> annotationClass) {
		T annotation = null;
		if (field != null)
			annotation = field.getAnnotation(annotationClass);
		return annotation;
	}

	public static Method findMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
		Method method = null;
		try {
			method = clazz.getMethod(methodName, paramTypes);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return method;
	}
}
