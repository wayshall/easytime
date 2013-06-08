package org.onetwo.core.codegenerator.generator.impl;

import java.util.Map;
import java.util.Set;

import org.onetwo.core.codegenerator.GeneratorConfig;
import org.onetwo.core.codegenerator.db.meta.TableInfo;
import org.onetwo.core.codegenerator.util.GenerateUtils;
import org.onetwo.core.codegenerator.util.TemplateContext;
import org.onetwo.core.util.StringUtils;

import freemarker.template.utility.StringUtil;

@SuppressWarnings("unchecked")
public class CommonlContextBuilder extends AbstractBuilder {
	
	private String templateFile;
	private String selfPackage;
	
	private boolean outFileNameCapitalize;
	
	public CommonlContextBuilder(){
		super(DefaultBuilder.common);
	}
	
	public CommonlContextBuilder(String name){
		super(name);
		String[] strs = StringUtil.split(name, '_');
		if(strs!=null && strs.length>1){
			String f = strs[strs.length-1];
//			setTemplateFile(f);
			String p = StringUtils.substringBefore(name, f).replace('_', '.');
			if(p.endsWith(".")){
				p = p.substring(0, p.length()-1);
			}
			setSelfPackage(p);
		}
		setTemplateFile(name);
	}

	public void setSelfPackage(String selfPackage) {
		if(selfPackage.startsWith(".")){
			selfPackage = selfPackage.substring(1);
		}
		this.selfPackage = selfPackage;
	}

	public void setTemplateFile(String templateFile) {
		this.templateFile = templateFile;
		outFileNameCapitalize = !templateFile.startsWith("_");
		if(!outFileNameCapitalize)
			templateFile = templateFile.substring(1);
		/*int index = templateFile.indexOf("_");
		if(index!=-1){
			this.selfPackage = templateFile.substring(0, index);
		}*/
	}
	
	protected String getFullPackage(GeneratorConfig config){
		String fp = config.getJavaPackage()+"."+selfPackage;
		return fp;
	}

	@Override
	public TemplateContext buildTheTemplateContext(final TableInfo table, final GeneratorConfig config, final Map context) {

		if(table.getPrimaryKey()==null){
			System.err.println("[" + table.getName() + "] no table primaryKey. service implements igonre generated file.");
			return null;
		}

		String selfClassName = table.getClassName()+getFileNamePostfix(templateFile);
		String fullPackage = getFullPackage(config);
		Set<String> classes = GenerateUtils.importClasses(table);
		context.put("builder", this);
		context.put("importClasses", classes);
		context.put("commonName", table.getClassName());

		context.put("selfClassName", selfClassName);
		context.put("selfPackage", selfPackage);
		context.put("commonPackage", config.getJavaPackage());
		context.put("basePackage", config.getJavaPackage());
		context.put("fullPackage", fullPackage);
		
		String outfileName = null;
		if(outFileNameCapitalize){
			outfileName = selfClassName;
		}else{
			outfileName = StringUtils.uncapitalize(selfClassName);
		}
		
		final String outfile = config.getGenerateOutDir() + "/" + fullPackage.replace('.', '/') + "/" + outfileName + "." + getFilePostfix(templateFile);
		
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
				return templateFile;
			}
			
		};
	}
	
	public static void main(String[] args){
		CommonlContextBuilder b = new CommonlContextBuilder("aa_entity_Entity.java.ftl");
		System.out.println(b.templateFile);
		System.out.println(b.getFileNamePostfix(b.templateFile));
		System.out.println("file postfix:"+b.getFilePostfix(b.templateFile));
		System.out.println(b.getFullPackage(GeneratorConfig.getInstance()));
	}
}
