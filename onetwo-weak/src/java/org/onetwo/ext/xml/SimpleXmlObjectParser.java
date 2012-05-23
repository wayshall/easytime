package org.onetwo.ext.xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.VisitorSupport;
import org.onetwo.core.exception.BaseException;
import org.onetwo.core.util.StringUtils;

@SuppressWarnings("unchecked")
public class SimpleXmlObjectParser extends VisitorSupport implements XmlObjectParser {
	
//	protected Logger logger = Logger.getLogger(this.getClass());
	
	private Map<String, ElementMapper> tagClassMap = new HashMap<String, ElementMapper>();
	private String rootTag = null;
	private Object root = null;
	private OgnlContext context;
	
	public SimpleXmlObjectParser(){
	}

	/* (non-Javadoc)
	 * @see org.onetwo.ext.xml.XmlObjectParser#map(java.lang.String, java.lang.Class, boolean)
	 */
	public SimpleXmlObjectParser map(String tag, Class clazz, boolean root){
		ElementMapper m = new ElementMapper(tag, clazz, root);
		this.tagClassMap.put(tag, m);
		return this;
	}
	
	public Object getObjectByTag(String tag){
		ElementMapper m = this.tagClassMap.get(tag);
		if(m==null)
			return null;
		else
			return m.createNodeObject();
	}

	@Override
	public void visit(Attribute node) {
		System.out.println(node.getParent().getName()+"  atrribute [ " + node.getName()+"=" + node.getText()+"|  " + node.getPath());
		this.setValue(node, node.getText());
	}

	@Override
	public void visit(Element node) {
//		System.out.println("Element [ " + node.getName()+"=" + "("+ node.getTextTrim()+")  " + node.getUniquePath());
		ElementMapper m = this.tagClassMap.get(node.getName());
		Object value = null;
		if(!node.isRootElement() && m==null){
			value = node.getTextTrim();
			setValue(node, value);
		}else if(node.isRootElement()){
			rootTag = node.getName();
			if(root==null)
				root = getObjectByTag(node.getName());
		}else{
			value = getObjectByTag(node.getName());
			ElementMapper pm = this.tagClassMap.get(node.getParent().getName());
			if(pm.isCollection()){
				List listProperty = (List) this.getNodeValue(node);
				if(listProperty==null){
					listProperty = new ArrayList();
					listProperty.add(null);//dom解释出来的path从[1]开始
					this.setValue(this.getNodeExpression(node), root, listProperty);
				}else if(listProperty.isEmpty()){
					listProperty.add(null);
				}
				listProperty.add(listProperty.size(), value);
			}
			this.setValue(node, value);
		}
	}
	
	public Object getNodeValue(Node node){
		String expression = this.getNodeExpression(node);
		Object value = null;
		if(StringUtils.isNotBlank(expression)){
			try {
				value = Ognl.getValue(expression, getContext(), root);
			} catch (OgnlException e) {
				throw new BaseException("getNodeValue error : " + e.getMessage(), e);
			}
		}else{
			value = root;
		}
		return value;
	}
	
	public String getNodeUniqueExpression(Node node){
		String exp = parsePath(node.getUniquePath());
		if(exp.startsWith("."))
			exp = exp.substring(1);
		return exp;
	}
	
	public String getNodeExpression(Node node){
		String exp = parsePath(node.getPath());
		if(exp.startsWith("."))
			exp = exp.substring(1);
		return exp;
	}
	
	protected String parsePath(String path){
		String exp = StringUtils.uncapitalize(path).replace('/', '.').replace("@", "").substring(this.rootTag.length()+1);
		return exp;
	}
	
	public void setValue(String expression, Object root, Object value){
		try {
			Ognl.setValue(expression, getContext(), root, value);
		} catch (OgnlException e) {
//			logger.error("setValue error : " + e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void setValue(Node node, Object value){
//		if("created_at".equalsIgnoreCase(node.getName()))
//			System.out.println("===>>> created_at : "+StringUtils.uncapitalize(node.getName()));
		String expression = this.getNodeUniqueExpression(node);
		setValue(expression, root, value);
	}

	/* (non-Javadoc)
	 * @see org.onetwo.ext.xml.XmlObjectParser#getRoot()
	 */
	public Object getRoot() {
		return root;
	}

	/* (non-Javadoc)
	 * @see org.onetwo.ext.xml.XmlObjectParser#setRoot(java.lang.Object)
	 */
	public XmlObjectParser setRoot(Object root) {
		this.root = root;
		return this;
	}

	/* (non-Javadoc)
	 * @see org.onetwo.ext.xml.XmlObjectParser#getContext()
	 */
	public OgnlContext getContext() {
		if(this.context==null){
			this.context = new OgnlContext();
		}
		return context;
	}

	/* (non-Javadoc)
	 * @see org.onetwo.ext.xml.XmlObjectParser#setContext(ognl.OgnlContext)
	 */
	public void setContext(OgnlContext context) {
		this.context = context;
	}

	public static void main(String[] args) throws IOException{
		/*Resource config = new ClassPathResource("bb_config.xml");
		Document doc = DomUtils.readXml(config.getFile());
		XmlParser p = new XmlParser();
		Map map = new HashMap();
		map.put("match", null);//根元素有那些属性
		p.setRoot(map);//设置根元素
		p.map("bbConfig", HashMap.class, true);//根元素对应类型
		p.map("match", BBCode.class, false);//映射各元素对应的类型
		doc.accept(p);
		Object root = p.getRoot();
		System.out.println(root.toString());*/
	}
}
