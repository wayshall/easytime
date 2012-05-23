package org.onetwo.core.codegenerator.generator.impl;

import java.util.Map;

import org.onetwo.core.codegenerator.GeneratorConfig;
import org.onetwo.core.codegenerator.db.meta.TableInfo;
import org.onetwo.core.codegenerator.util.TemplateContext;

@SuppressWarnings("unchecked")
public class DaoContextBuilder extends AbstractBuilder {
	
	public DaoContextBuilder(){
		super(DefaultBuilder.dao);
	}
 
	@Override
	public TemplateContext buildTheTemplateContext(final TableInfo table, final GeneratorConfig config, final Map context) {
		if(table.getPrimaryKey()==null){
			System.err.println("[" + table.getName() + "] no table primaryKey. dao igonre generated file.");
			return null;
		}
		
//		Set<String> classes = GenerateUtils.importClasses(table);
//		context.put("importClasses", classes);
		context.put("commonName", table.getClassName());
		context.put("daoClassName", table.getClassName()+config.getDaoNamePostfix());
		context.put("serviceClassName", table.getClassName()+config.getServiceNamePostfix());
		context.put("modelClassName", table.getClassName()+config.getModelNamePostfix());
		context.put("commonPackage", config.getJavaPackage());
		context.put("daoPackage", config.getDaoPackage());
		context.put("servicePackage", config.getServicePackage());
		context.put("modelPackage", config.getModelPackage());
		String templateFile = config.getDaoPackage();
		final String outfile = config.getGenerateOutDir() + "/" + config.getDaoPackage().replace('.', '/') +"/" + table.getClassName()+getFileNamePostfix(templateFile) + "." + getFilePostfix(templateFile);
		
		return new TemplateContext(){

			@Override
			public Object get(String key) {
				return context.get(key)==null?"":context.get(key);
			}

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
				return config.getDaoTemplate();
			}
			
		};
	}
}
