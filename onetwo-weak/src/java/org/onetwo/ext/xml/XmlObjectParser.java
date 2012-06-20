package org.onetwo.ext.xml;

import ognl.OgnlContext;

import org.dom4j.Visitor;

@SuppressWarnings("unchecked")
public interface XmlObjectParser extends Visitor {

	public SimpleXmlObjectParser map(String tag, Class clazz, boolean root);

	public Object getRoot();

	public XmlObjectParser setRoot(Object root);

	public OgnlContext getContext();

	public void setContext(OgnlContext context);

}