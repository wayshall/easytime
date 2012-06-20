package org.onetwo.core.codegenerator.generator.impl;

import java.util.HashMap;
import java.util.Map;

import org.onetwo.core.codegenerator.GeneratorConfig;
import org.onetwo.core.codegenerator.GeneratorFactory;
import org.onetwo.core.codegenerator.db.meta.TableInfo;
import org.onetwo.core.codegenerator.util.TemplateContext;
import org.onetwo.core.codegenerator.util.TemplateContextBuilder;
import org.onetwo.core.util.FileUtils;


@SuppressWarnings({ "unchecked", "serial" })
abstract public class AbstractBuilder implements TemplateContextBuilder {

	public static class DefaultBuilder {
		public static final String common = "common";
		public static final String entity = "entity";
		public static final String dao = "dao";
		public static final String service = "service";
		public static final String service_impl = "service_impl";
		
		private static Map<String, TemplateContextBuilder> values = new HashMap<String, TemplateContextBuilder>(){
			{
				put(common, new CommonlContextBuilder());
				put(entity, new ModelContextBuilder());
				put(dao, new DaoContextBuilder());
				put(service, new ServiceContextBuilder());
				put(service_impl, new ServiceImplContextBuilder());
			}
		};
		
		public static TemplateContextBuilder getBuilder(String name){
			TemplateContextBuilder builder = values.get(name);
			if(builder==null){
				builder = new CommonlContextBuilder(name);
			}
			return builder;
		}
	}

	private GeneratorFactory generatorFactory;
	private String name;
	
	public AbstractBuilder(String name){
		this.name = name;
	}
	
	protected Object getContextBean(){
		if(this.generatorFactory==null)
			return null;
		return this.generatorFactory.getContextBean();
	}

	@Override
	public TemplateContext buildTemplateContext(final TableInfo table, final GeneratorConfig config, final Map ctx) {
		table.setPrefix(config.getTablePrefix());
		final Map context = new HashMap();
		if(ctx!=null)
			context.putAll(ctx);
		context.putAll(config.getConfig());
		context.put("table", table);
		Object contextBean = getContextBean();
		if(contextBean!=null){
			context.put("contextBean", contextBean);
		}
		TemplateContext templateConext = buildTheTemplateContext(table, config, context);
		return templateConext;
	}
	
	abstract protected TemplateContext buildTheTemplateContext(final TableInfo table, final GeneratorConfig config, final Map ctx);
	
	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setGeneratorFactory(GeneratorFactory generatorFactory) {
		this.generatorFactory = generatorFactory;
	}
	
	public String getFileNamePostfix(String templateFile){
		String postfix = templateFile.substring(0, templateFile.indexOf('.'));
		int index = postfix.lastIndexOf('_');
		if(index!=-1)
			postfix = postfix.substring(index+1);
		return postfix;
	}
	
	public String getFilePostfix(String templateFile){
		String postfix = FileUtils.getExtendName(FileUtils.getFileNameWithoutExt(templateFile));
		return postfix;
	}
	
	public static void main(String[] args){
		String templateFile = "service_Service.java";
		System.out.println(FileUtils.getFileNameWithoutExt(templateFile));
		System.out.println(FileUtils.getExtendName(templateFile));
	}

}
