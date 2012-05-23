package org.onetwo.core.codegenerator.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.onetwo.core.codegenerator.db.meta.ColumnInfo;
import org.onetwo.core.codegenerator.db.meta.TableInfo;

@SuppressWarnings("unchecked")
abstract public class AbstractTemplateContext implements TemplateContext{

	protected TemplateContext templateContext;
	
	protected String template;
	
	protected Map context = new HashMap();
	
	protected String outfile;

	@Override
	public Map getContext() {
		return context;
	}

	@Override
	public String getOutfile() {
		return outfile;
	}

	@Override
	public String getTemplate() {
		return template;
	}
	
	public static Set<String> importClasses(TableInfo table){
		Set<String> importClasses = new HashSet<String>();
		addImportClass(table.getPrimaryKey().getJavaType(), importClasses);
		for(ColumnInfo column : table.getColumns().values()){
			addImportClass(column.getJavaType(), importClasses);
			if(column.isDateType()){
				addImportClass(Temporal.class, importClasses);
				addImportClass(TemporalType.class, importClasses);
			}
		}
		return importClasses;
	}
	
	public static void addImportClass(Class clazz, Set<String> importClasses){
		String pack = clazz.getPackage().getName();
		if(!pack.equals("java.lang")){
			importClasses.add(clazz.getName());
		}
	}

	public Object get(String key){
		return this.getContext().get(key);
	}
	
}