package org.onetwo.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.onetwo.core.exception.ServiceException;

@SuppressWarnings("unchecked")
public class AnnotationUtils {

	private AnnotationUtils() {
	}

	public static <T extends Annotation> T findAnnotation(Class<?> clazz, Class<T> annotationClass) {
		T annotation = clazz.getAnnotation(annotationClass);
		return annotation;
	}

	public static Field findFirstField(Class<?> clazz, Class annotationClass) {
		List<Field> fields = findAnnotationField(clazz, annotationClass);
		if(fields==null || fields.isEmpty())
			return null;
		return fields.get(0);
	}

	public static List<Field> findAnnotationField(Class<?> clazz, Class annotationClass) {
		List<Field> fields = ReflectUtils.getFields(clazz);
		if(fields==null || fields.isEmpty())
			return null;
		Annotation annotation = null;
		List<Field> annoFields = new ArrayList<Field>();
		for(Field f : fields){
			annotation = f.getAnnotation(annotationClass);
			if(annotation!=null)
				annoFields.add(f);
		}
		return annoFields;
	}

	public static <T extends Annotation> T findAnnotation(Class<?> clazz, String methodName, Class<T> annotationClass) {
		Method method = null;
		try {
			method = clazz.getMethod(methodName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return findAnnotation(clazz, method, annotationClass);
	}

	public static <T extends Annotation> T findAnnotation(Class<?> clazz, Method method, Class<T> annotationClass) {
		T annotation = null;
		if (method != null)
			annotation = method.getAnnotation(annotationClass);
		if (annotation == null)
			annotation = clazz.getAnnotation(annotationClass);
		if (annotation == null) {
			Class<?> parent = clazz.getSuperclass();
			if (parent == null || parent == Object.class)
				return null;

			Method parentMethod = null;
			if (method != null)
				parentMethod = findMethod(parent, method.getName(), method.getParameterTypes());
			return findAnnotation(parent, parentMethod, annotationClass);
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
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return method;
	}
}
