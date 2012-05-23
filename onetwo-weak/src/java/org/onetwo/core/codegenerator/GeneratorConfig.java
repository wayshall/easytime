package org.onetwo.core.codegenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.onetwo.core.properties.PropConfig;
import org.onetwo.core.util.StringUtils;

public class GeneratorConfig extends PropConfig {

	public static final String TEMPLATE_DIR = "template.dir";
	public static final String GENERATE_OUT_DIR = "generate.out.dir";
	public static final String DB_CONFIG = "db.config";
	
	public static final String ENCODING = "encoding";
	public static final String JAVA_PAGECKAGE = "base.pageckage";
	public static final String TABLE_PREFIX = "table.prefix";

	public static final String MODEL_TEMPLATE = "model.template";
	public static final String MODEL_CLASSNAME_POSTFIX = "model.className.postfix";
	public static final String MODEL_PACKAGE = "model.package";
	
	public static final String DAO_TEMPLATE = "dao.template";
	public static final String DAO_CLASSNAMEE_POSTFIX = "dao.className.postfix";
	public static final String DAO_PACKAGE = "dao.package";
	
	public static final String SERVICE_TEMPLATE = "service.template";
	public static final String SERVICE_CLASSNAME_POSTFIX = "service.className.postfix";
	public static final String SERVICE_PACKAGE= "service.package";
	
	public static final String SERVICE_IMPL_TEMPLATE = "serviceImpl.template";
	public static final String SERVICE_IMPL_CLASSNAME_POSTFIX = "serviceImpl.className.postfix";
	public static final String SERVICE_IMPL_PACKAGE= "serviceImpl.package";
	
	public static final String GENERATE_TABLES = "generate.tables";
	
	
	public static final String COMPONENT_PRIMARY_FACTORY = "component.PrimaryKeyFactory";
	public static final String COMPONENT_PRIMARY = "component.PrimaryKey";

	public static final String COMPONENT_TABLEINFO = "component.TableInfo";
	
	public static final String GENERATE_CONTEXTBEAN = "generate.contextBean";
	
//	public static final String BEANFACTORY_CLASS = "beanfactory.class";
	
//	public static final String TABLEMANAGER_CLASS = "table.manager.class";
	
	public static GeneratorConfig instance = null;
	
	private Properties dbconfigProp;
	
	public static GeneratorConfig getInstance(){
		return getInstance(null);
	}
	
	public static GeneratorConfig getInstance(String config){
		if(instance==null){
			if(StringUtils.isBlank(config))
				instance = new GeneratorConfig();
			else
				instance = new GeneratorConfig(config);
		}
		return instance;
	}
	
	private GeneratorConfig(){
		super("generator/generator.properties", false);
	}
	
	public GeneratorConfig(String config){
		super(config);
	}
	
	public String getTemplateDir(){
		return this.getProperty(TEMPLATE_DIR);
	}
	
	public String getGenerateOutDir(){
		return this.getProperty(GENERATE_OUT_DIR);
	}
	
	public String getModelTemplate(){
		return this.getProperty(MODEL_TEMPLATE);
	}

	public String getModelNamePostfix(){
		return this.getProperty(MODEL_CLASSNAME_POSTFIX, "");
	}
	
	public String getModelPackage(){
		return this.getProperty(MODEL_PACKAGE);
	}

	
	public String getDaoNamePostfix(){
		return this.getProperty(DAO_CLASSNAMEE_POSTFIX, "");
	}
	
	public String getDaoTemplate(){
		return this.getProperty(DAO_TEMPLATE);
	}
	
	public String getDaoPackage(){
		return this.getProperty(DAO_PACKAGE);
	}

	
	public String getServiceTemplate(){
		return this.getProperty(SERVICE_TEMPLATE);
	}
	public String getServiceNamePostfix(){
		return this.getProperty(SERVICE_CLASSNAME_POSTFIX, "");
	}
	public String getServicePackage(){
		return this.getProperty(SERVICE_PACKAGE);
	}

	
	public String getServiceImplTemplate(){
		return this.getProperty(SERVICE_IMPL_TEMPLATE);
	}
	public String getServiceImplNamePostfix(){
		return this.getProperty(SERVICE_IMPL_CLASSNAME_POSTFIX, "");
	}
	public String getServiceImplPackage(){
		return this.getProperty(SERVICE_IMPL_PACKAGE);
	}
	
	public String getJavaPackage(){
		return this.getProperty(JAVA_PAGECKAGE);
	}
	
	public String getEncoding(){
		return this.getProperty(ENCODING);
	}
	
	public String getGenerateTables(){
		return this.getProperty(GENERATE_TABLES);
	}
	
	public List<String> getGenerateTableList(){
		String str = this.getGenerateTables();
		if(StringUtils.isBlank(str))
			return null;
		String[] tables = str.split(",");
		List<String> list = new ArrayList<String>();
		for(String table : tables){
			if(StringUtils.isNotBlank(table))
				list.add(table);
		}
		return list;
	}
	
	public String getOutPageckagePath(){
		return this.getGenerateOutDir() + "/" + this.getJavaPackage().replace('.', '/');
	}
	
	public String getDbconfig(){
		return this.getProperty(DB_CONFIG);
	}
	
	public String getTablePrefix(){
		return this.getProperty(TABLE_PREFIX);
	}

	public Properties getDbconfigProp() {
		return dbconfigProp;
	}

	public void setDbconfigProp(Properties dbconfigProp) {
		this.dbconfigProp = dbconfigProp;
	}
	
	/*public String getBeanFactoryClass(){
		return this.getProperty(BEANFACTORY_CLASS);
	}

	public String getTableManager(){
		return this.getProperty(TABLEMANAGER_CLASS);
	}*/
}
