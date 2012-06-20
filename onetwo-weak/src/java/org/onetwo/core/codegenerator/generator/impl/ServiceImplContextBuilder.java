package org.onetwo.core.codegenerator.generator.impl;

import java.util.Map;

import org.onetwo.core.codegenerator.GeneratorConfig;
import org.onetwo.core.codegenerator.db.meta.TableInfo;
import org.onetwo.core.codegenerator.util.TemplateContext;
import org.onetwo.core.codegenerator.util.TemplateContext.FileType;

@SuppressWarnings("unchecked")
public class ServiceImplContextBuilder extends AbstractBuilder {
	
	public ServiceImplContextBuilder(){
		super(DefaultBuilder.service_impl);
	}

	@Override
	public TemplateContext buildTheTemplateContext(final TableInfo table, final GeneratorConfig config, final Map context) {

		if(table.getPrimaryKey()==null){
			System.err.println("[" + table.getName() + "] no table primaryKey. service implements igonre generated file.");
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
		
		context.put("serviceImplclassName", table.getClassName()+config.getServiceImplNamePostfix());
		context.put("serviceImplPackage", config.getServiceImplPackage());
		
		String templateFile = config.getServiceImplTemplate();
		final String outfile = config.getGenerateOutDir() + "/" + config.getServiceImplPackage().replace('.', '/') +"/" + table.getClassName()+getFileNamePostfix(templateFile) + "." + getFilePostfix(templateFile);
		
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
				return config.getServiceImplTemplate();
			}
			
		};
	}
}
