package org.onetwo.core.util;


import java.util.LinkedHashMap;
import java.util.Map;


@SuppressWarnings("unchecked")
public class SimpleExpression implements Expression {

	public static final SimpleExpression DOLOR_INSTANCE = new SimpleExpression("${", "}");
	public static final SimpleExpression WELL_INSTANCE = new SimpleExpression("#{", "}");

	protected String start;
	protected String end;
	protected String text;
	protected Map<String, Integer> varIndexMap = new LinkedHashMap<String, Integer>();

	protected Object valueProvider;
	public SimpleExpression(String start, String end, Object valueProvider) {
		this(start, end);
		this.valueProvider = valueProvider;
	}

	public SimpleExpression(String start, String end) {
		this.start = start;
		this.end = end;
	}

	public boolean isExpresstion(String text) {
		return StringUtils.isNotBlank(text) && text.indexOf(start) != -1 && text.indexOf(end) != -1;
	}

	public boolean isExpresstion() {
		return this.isExpresstion(this.text);
	}

	public String parse(String text) {
		return parse(text, this.valueProvider);
	}

	public String parse(String text, Object... objects) {
		if (!isExpresstion(text))
			return text;

		final Map context = MyUtils.convertParamMap(objects);
		text = parse(text, context);

		return text;
	}

	public String parse(ValueProvider provider) {
		return parse(this.text, provider);
	}

	public String parse(String text, Object provider) {
		Assert.notNull(provider, "provider can not be null!");
		if (StringUtils.isBlank(text))
			return "";

		int beginIndex = text.indexOf(start);
		if (beginIndex == -1)
			return text;

		String var = null;
		int varIndex = 1;
		StringBuilder sb = new StringBuilder();
		int off = 0;
		while (beginIndex != -1) {
			int endIndex = text.indexOf(end, beginIndex);
			if (endIndex == -1)
				break;

			sb.append(text.substring(off, beginIndex));

			var = text.substring(beginIndex + start.length(), endIndex);
			// sb.delete(beginIndex, endIndex+1);
			if (StringUtils.isBlank(var))
				continue;
			String value = getValueByVar(provider, var);
			// sb.insert(beginIndex, value);
			if (value != null)
				sb.append(value);
			beginIndex = text.indexOf(start, endIndex);
			off = endIndex + end.length();

			this.varIndexMap.put(var, varIndex++);
		}
		if (off < text.length()) {
			sb.append(text.substring(off));
		}
		return sb.toString();
	}
	
	protected String getValueByVar(Object provider, String var){
		if(provider==null)
			return null;
		
		if(provider instanceof ValueProvider)
			return ((ValueProvider) provider).findString(var);
		else if(provider instanceof Map)
			return (String)((Map)provider).get(var);
		throw new IllegalArgumentException("provider must be a ValueProvider type : " + provider.getClass());
	}

	public Integer getVarIndex(String var) {
		Integer index = this.varIndexMap.get(var);
		return index;
	}

	/***************************************************************************
	 * 变量与位置的映射 如：文本"text${var1}test${var2}test"经过SimpleExpression解释后
	 * 通过此方法可返回下面的映射： var1 --> 1 var2 --> 2
	 * 
	 * @return
	 */
	public Map<String, Integer> getVarIndexMap() {
		return varIndexMap;
	}

	public static void main(String[] args) {
		String str = "ads${bb}asdf";
		Expression sp = new SimpleExpression("${", "}");
		System.out.println(sp.isExpresstion(str));
		sp.parse(str, "bb", "test");
		System.out.println(sp.getVarIndex("bb"));
	}

}
