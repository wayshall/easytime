package org.onetwo.ext.parametied;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import org.junit.Test;

public class ModelTest {
	
	public static class SubModel extends Model<Long> {
		
	}
	@Test
	public void test(){
		Model model = new Model();
		Class cls = model.getClass();
		Type gentype = cls.getGenericSuperclass();
		System.out.println("gentype: " + gentype);
		Method[] methods = cls.getMethods();
		for (Method method : methods) {
			Type returnType = method.getGenericReturnType();
			Type[] ptypes = method.getGenericParameterTypes();
			
			if(returnType instanceof TypeVariable){
				System.out.println("返回类型泛型方法：" + method);
			}else{
				for(Type t : ptypes){
					if(t instanceof TypeVariable)
						System.out.println("参数类型泛型方法：" + method);
				}
			}
			
		}

	}

}
