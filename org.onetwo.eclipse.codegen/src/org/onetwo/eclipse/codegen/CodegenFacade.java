package org.onetwo.eclipse.codegen;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.onetwo.core.codegenerator.GeneratorConfig;
import org.onetwo.core.codegenerator.GeneratorFacade;
import org.onetwo.core.codegenerator.GeneratorFactory;

public interface CodegenFacade {

	public boolean isDbconnectSuccess(Properties dbconfig);
	
	public GeneratorConfig getConfig();
	
	public GeneratorFactory getFactory();
	public Collection<String> getTableNames(boolean refresh);
	public String getDatabaseName();
	public void generateTable(String table);
	
	public void generateTable(List<String> tables);
	
	public List<String> getTemplates();

	public CodegenFacade addBuilder(String name);
	
	public CodegenFacade removeBuilder(String name);
	
	public String getDefaultTemplateDir();
	
	public void destroy();
	
	public CodegenFacade clearBuilders();
	
}