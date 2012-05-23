package org.onetwo.core.util;

import java.lang.reflect.Array;

@SuppressWarnings("unchecked")
public abstract class ArrayUtils {
    public static final String[] EMPTY_STRING_ARRAY = new String[0];

	public static Object[] add(Object[] array, Object element) {
        Class type = (array != null ? array.getClass() : (element != null ? element.getClass() : Object.class));
        Object[] newArray = (Object[]) copyArrayGrow1(array, type);
        newArray[newArray.length - 1] = element;
        return newArray;
    }    
    
    private static Object copyArrayGrow1(Object array, Class newArrayComponentType) {
        if (array != null) {
            int arrayLength = Array.getLength(array);
            Object newArray = Array.newInstance(array.getClass().getComponentType(), arrayLength + 1);
            System.arraycopy(array, 0, newArray, 0, arrayLength);
            return newArray;
        }
        return Array.newInstance(newArrayComponentType, 1);
    }
}
