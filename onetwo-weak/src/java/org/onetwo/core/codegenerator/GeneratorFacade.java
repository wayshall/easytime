package org.onetwo.core.codegenerator;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.core.codegenerator.db.meta.TableInfo;
import org.onetwo.core.codegenerator.generator.TableInfoConvertor;
import org.onetwo.core.codegenerator.generator.impl.AbstractBuilder;
import org.onetwo.core.codegenerator.util.TemplateContextBuilder;


@SuppressWarnings("unchecked")
public class GeneratorFacade {
	
//	private static GeneratorFacade generatorFacade;
	
	private GeneratorFactory factory;
	
	private Map<String, TemplateContextBuilder> builders = new HashMap<String, TemplateContextBuilder>();
	
	/*public static GeneratorFacade getInstance(){
		if(generatorFacade==null){
			final GeneratorFactory f = new DefaultGeneratorFactory(GeneratorConfig.getInstance());
			generatorFacade = new GeneratorFacade(f){
				{
//					this.addContext(new ModelGenerator(f.getConfig(), f.getFreemarkerTemplate()))
//						.addContext(new DaoContext(f.getConfig(), f.getFreemarkerTemplate()))
//						.addContext(new ServiceGenerator(f.getConfig(), f.getFreemarkerTemplate()));
				}
			};
		}
		return generatorFacade;
	}*/
	
	public GeneratorFacade(GeneratorFactory factory){
		this.factory = factory;
	}
	
	public GeneratorFactory getFactory() {
		return factory;
	}

	public GeneratorFacade addContextBuilder(TemplateContextBuilder builder){
		if(builder instanceof AbstractBuilder){
			((AbstractBuilder)builder).setGeneratorFactory(factory);
		}
		this.builders.put(builder.getName(), builder);
		return this;
	}

	public GeneratorFacade removeContextBuilder(String name){
		this.builders.remove(name);
		return this;
	}

	public void generateAllTables() {
		Collection<TableInfo> tables = factory.getTableManager().getTables();
		for(TableInfo table : tables){
			this.generateFile(table, null);
		}
		openOutdir();
	}
	


	public void generateConfigTables() {
		List<String> tables = factory.getConfig().getGenerateTableList();
		generateTables(tables);
	}

	public void generateTables(List<String> tables) {
		if(tables==null){
			System.out.println("no tables!");
			return ;
		}
		List<TableInfo> tableInfos = factory.getTableManager().getTables(tables);
		for(TableInfo table : tableInfos){
			this.generateFile(table, null);
		}
//		openOutdir();
	}


	public void generateFile(String tableName, Map context) {
		this.generateFile(factory.getTableManager().getTable(tableName), context);
	}

	public void generateFile(TableInfo table, Map context) {
		if(this.builders==null || this.builders.isEmpty())
			return ;
		
		for(TemplateContextBuilder builder : this.builders.values())
			factory.getFreemarkerTemplate().generateFile(builder.buildTemplateContext(table, factory.getConfig(), context));
	}

	public void generateFileByModelClass(Class clazz, TableInfoConvertor convertor, Map context) {
		this.generateFile(convertor.convert(clazz), context);
	}
	
	public void openOutdir(){
		try {
			Runtime.getRuntime().exec("cmd.exe /c start " + factory.getConfig().getOutPageckagePath());
		} catch (IOException e) {
			throw new RuntimeException("open dir ["+factory.getConfig().getTemplateDir()+"] error!");
		}
	}

	public Map<String, TemplateContextBuilder> getBuilders() {
		return builders;
	}

}
