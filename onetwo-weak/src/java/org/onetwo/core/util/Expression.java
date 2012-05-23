package org.onetwo.core.util;

public interface Expression {

	public boolean isExpresstion(String text);

	public boolean isExpresstion();

	public String parse(String text);

	public String parse(String text, Object... objects);

	public String parse(ValueProvider provider);

	public String parse(String text, Object provider);

	public Integer getVarIndex(String var);

}