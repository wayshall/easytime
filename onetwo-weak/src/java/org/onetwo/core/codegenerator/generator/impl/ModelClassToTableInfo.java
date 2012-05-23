package org.onetwo.core.codegenerator.generator.impl;

import java.lang.reflect.Field;

import javax.persistence.Id;

import org.onetwo.core.codegenerator.db.meta.PrimaryKey;
import org.onetwo.core.codegenerator.db.meta.TableInfo;
import org.onetwo.core.codegenerator.generator.TableInfoConvertor;
import org.onetwo.core.exception.BaseException;
import org.onetwo.core.util.AnnotationUtils;

@SuppressWarnings("unchecked")
public class ModelClassToTableInfo implements TableInfoConvertor{

	@Override
	public TableInfo convert(Object obj) {
		if(!(obj instanceof Class))
			throw new BaseException("the object is not the instance of java.lang.Class!");
		Class clazz = (Class) obj;
		TableInfo tableInfo = new TableInfo(clazz.getSimpleName());
		Field id = AnnotationUtils.findFirstField(clazz, Id.class);
		PrimaryKey pk = new PrimaryKey();
		pk.setName(id.getName());
		pk.setJavaType(id.getType());
		tableInfo.setPrimaryKey(pk);
		return tableInfo;
	}

}
