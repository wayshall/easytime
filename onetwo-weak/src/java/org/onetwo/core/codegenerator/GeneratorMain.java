package org.onetwo.core.codegenerator;

import org.onetwo.core.codegenerator.generator.impl.CommonlContextBuilder;
import org.onetwo.core.codegenerator.impl.DefaultGeneratorFactory;




public class GeneratorMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		GodGeneratorFacade.getInstance().generateConfigTables();
//		GeneratorFacade.getInstance().generateConfigTables();

		final GeneratorConfig config = GeneratorConfig.getInstance();
		GeneratorFactory factory = new DefaultGeneratorFactory(config);
		factory.init(config.getDbconfigProp());
		
		GeneratorFacade g = new GeneratorFacade(factory)
//								.addContextBuilder(new DaoContextBuilder())
//								.addContextBuilder(new ServiceContextBuilder())
//								.addContextBuilder(new ServiceImplContextBuilder())
								.addContextBuilder(new CommonlContextBuilder(){
									{
										this.setTemplateFile(config.getModelTemplate());
									}
								});
//		g.generateAllTables();
		g.generateConfigTables();
		/*TableManager tm = factory.getTableManager();
		String dbname = factory.getDatabaseManager().getDatabaseName();
		System.out.println("dbname: "+dbname);*/
	}

}
