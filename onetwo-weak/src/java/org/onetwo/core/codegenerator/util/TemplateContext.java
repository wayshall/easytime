package org.onetwo.core.codegenerator.util;

import java.util.Map;

@SuppressWarnings("unchecked")
public interface TemplateContext {

	public static final String CONFIG_KEY = "config";
	public static final String TABLE_KEY = "table";
	
	public static enum FileType {
		MODEL("java"), 
		DAO("java"), 
		SERVICE("java"), 
		JAVA("java"), 
		JSP("jsp"); 
		
		private String postfix;
		
		FileType(String postfix){
			this.postfix = postfix;
		}
		
		public String getPostfix(){
			return this.postfix;
		}
		
		public String toString(){
			return super.toString().toLowerCase();
		}
	}
	
	public String getOutfile();
	
	public String getTemplate();
	
	public Map getContext();
	
	public Object get(String key);
	
}
