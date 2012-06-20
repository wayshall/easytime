package org.onetwo.ext.xml;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.core.util.ReflectUtils;

@SuppressWarnings("unchecked")
public class ElementMapper {

	private String nodeName;
	private Class clazz;
	private boolean collection;

	public ElementMapper() {
	}

	public ElementMapper(String nodeName, Class clazz, boolean collection) {
		super();
		this.nodeName = nodeName;
		this.clazz = clazz;
		this.collection = collection;
	}

	public Object createNodeObject() {
		Object obj = null;
		if (this.clazz == List.class) {
			obj = new ArrayList();
			this.collection = true;
		} else
			obj = ReflectUtils.newInstance(clazz);
		return obj;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}

	public boolean isCollection() {
		return collection;
	}

	public void setCollection(boolean collection) {
		this.collection = collection;
	}

}
