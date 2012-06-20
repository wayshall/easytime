package org.onetwo.eclipse.codegen;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.onetwo.core.codegenerator.GeneratorConfig;
import org.onetwo.core.codegenerator.GeneratorFacade;
import org.onetwo.core.codegenerator.GeneratorFactory;
import org.onetwo.core.codegenerator.db.DatabaseManager;
import org.onetwo.core.codegenerator.generator.impl.AbstractBuilder.DefaultBuilder;
import org.onetwo.core.codegenerator.impl.DefaultGeneratorFactory;
import org.onetwo.core.codegenerator.util.TemplateContextBuilder;
import org.onetwo.core.exception.ServiceException;
import org.onetwo.eclipse.PluginUtils;

@SuppressWarnings("unchecked")
public class DefaultCodegenFacade implements CodegenFacade {
	
	private GeneratorConfig config;
	private GeneratorFactory factory;
	private GeneratorFacade generatorFace;
	private String defaultTemplateDir;
	private boolean init;
	
	DefaultCodegenFacade(){
		config = GeneratorConfig.getInstance(PluginUtils.getPluginPath()+"/generator.properties");
		defaultTemplateDir = PluginUtils.getPluginPath()+"/template";
		factory = new DefaultGeneratorFactory(config);
		generatorFace = new GeneratorFacade(factory);
	}
	
	public boolean isInit() {
		return init;
	}
	
	public void checkStatus(){
		if(isInit())
			return ;
		factory.init(CodegenUtils.getDatabaseConfig(CodegenPlugin.getDefault().getPreferenceStore()));
	}

	public boolean isDbconnectSuccess(Properties dbconfig){
		try {
			factory.init(dbconfig);
			DatabaseManager dm = factory.getDatabaseManager();
			dm.initDatabaseManager();
			return dm.isConnectSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String getDatabaseName(){
		checkStatus();
		return factory.getDatabaseManager().getDatabaseName();
	}
	
	public Collection<String> getTableNames(boolean refresh){
		checkStatus();
		try {
			Collection<String> tables = factory.getTableManager().getTableNames(refresh);
			return tables;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public GeneratorConfig getConfig() {
		return config;
	}

	public GeneratorFactory getFactory() {
		return factory;
	}
	
	public void generateTable(String table){
		checkStatus();
		generatorFace.generateFile(table, null);
	}
	
	public void generateTable(List<String> tables){
		try {
			checkStatus();
			this.factory.getFreemarkerTemplate().init();
			generatorFace.generateTables(tables);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("generate table error : " + e.getMessage(), e);
		}
	}
	
	public List<String> getTemplates(){
		File templateDir = new File(this.config.getTemplateDir());
		String[] templates = templateDir.list(new FilenameFilter(){

			@Override
			public boolean accept(File dir, String name) {
				if(name.endsWith(".ftl") || name.endsWith(".java") || name.endsWith(".jsp"))
					return true;
				return false;
			}
			
		});
		
		if(templates==null || templates.length==0)
			return null;
		
		List<String> tempNames = new ArrayList<String>();
		for(String f : templates){
//			f = FileUtils.getFileNameWithoutExt(f);
			tempNames.add(f);
		}
		return tempNames;
	}
	
	public CodegenFacade addBuilder(String name){
		TemplateContextBuilder builder = DefaultBuilder.getBuilder(name);
		this.generatorFace.addContextBuilder(builder);
		return this;
	}
	
	public CodegenFacade clearBuilders(){
		this.generatorFace.getBuilders().clear();
		return this;
	}
	
	public CodegenFacade removeBuilder(String name){
		this.generatorFace.removeContextBuilder(name);
		return this;
	}

	public String getDefaultTemplateDir() {
		return defaultTemplateDir;
	}
	
	public void destroy(){
		this.generatorFace = null;
		this.factory = null;
		this.config = null;
		this.defaultTemplateDir = null;
	}

	

}
