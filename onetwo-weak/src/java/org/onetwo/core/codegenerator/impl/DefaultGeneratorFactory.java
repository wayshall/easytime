package org.onetwo.core.codegenerator.impl;

import java.util.Properties;

import org.onetwo.core.beanfactory.AfterConstruction;
import org.onetwo.core.beanfactory.BeanFactory;
import org.onetwo.core.beanfactory.impl.DefaultBeanFactory;
import org.onetwo.core.codegenerator.ColumnInfoFactory;
import org.onetwo.core.codegenerator.GeneratorConfig;
import org.onetwo.core.codegenerator.GeneratorFactory;
import org.onetwo.core.codegenerator.PrimaryKeyFactory;
import org.onetwo.core.codegenerator.TableInfoFactory;
import org.onetwo.core.codegenerator.db.DatabaseManager;
import org.onetwo.core.codegenerator.db.DefaultDatabaseManage;
import org.onetwo.core.codegenerator.db.DefaultTableManager;
import org.onetwo.core.codegenerator.db.TableManager;
import org.onetwo.core.codegenerator.util.FreemarkerTemplate;

@SuppressWarnings("unchecked")
public class DefaultGeneratorFactory implements GeneratorFactory{
	
	protected GeneratorConfig config;
	
	protected BeanFactory beanFactory;
	
	protected DatabaseManager databaseManager;
	
	protected TableManager tableManager;
	
	public DefaultGeneratorFactory(GeneratorConfig config){
		this.config = config;
		this.beanFactory = new DefaultBeanFactory();
	}
	
	public void init(Properties dbconfig){
		databaseManager = beanFactory.getBean(DefaultDatabaseManage.class, true);
		if(dbconfig!=null && !dbconfig.isEmpty())
			databaseManager.setDbconfig(dbconfig);
		else
			databaseManager.setDbconfigPath(config.getDbconfig());
		databaseManager.initDatabaseManager();
		
		tableManager = beanFactory.getBean(DefaultTableManager.class, true, new AfterConstruction<DefaultTableManager>(){
			@Override
			public void afterConstruction(DefaultTableManager tableManager) {
				proccessBean(tableManager);
				tableManager.setDatabaseManager(databaseManager);
				tableManager.setTableFactory(getTableInfoFactory());
				tableManager.setColumnFactory(getColumnInfoFactory());
				tableManager.setPkFactory(getPrimaryKeyFactory());
			}
		});
	}

	@Override
	public ColumnInfoFactory getColumnInfoFactory(){
		ColumnInfoFactory f = beanFactory.getBean(DefaultColumnInfoFactory.class, true, afterConstruction());
		return f;
	}
	
	protected AfterConstruction afterConstruction(){
		return new AfterConstruction(){
			@Override
			public void afterConstruction(Object bean) {
				proccessBean(bean);
			}
		};
	}

	@Override
	public PrimaryKeyFactory getPrimaryKeyFactory(){
		return (PrimaryKeyFactory) beanFactory.getBean(this.config.getClass(GeneratorConfig.COMPONENT_PRIMARY_FACTORY), true, afterConstruction());
	}

	@Override
	public Object getContextBean(){
		Class cls = this.config.getClass(GeneratorConfig.GENERATE_CONTEXTBEAN);
		if(cls==null)
			return null;
		return beanFactory.getBean(cls, true, afterConstruction());
	}

	@Override
	public TableInfoFactory getTableInfoFactory(){
		return beanFactory.getBean(DefaultTableInfoFactory.class, true, afterConstruction());
	}

	@Override
	public GeneratorConfig getConfig() {
		return config;
	}

	@Override
	public DatabaseManager getDatabaseManager() {
		return databaseManager;
	}

	@Override
	public FreemarkerTemplate getFreemarkerTemplate() {
		return beanFactory.getBean(FreemarkerTemplate.class, false, new AfterConstruction<FreemarkerTemplate>(){
			@Override
			public void afterConstruction(FreemarkerTemplate bean) {
				proccessBean(bean);
				bean.setEncoding(getConfig().getEncoding());
				bean.setTemplateDir(getConfig().getTemplateDir());
			}
		});
	}

	@Override
	public TableManager getTableManager() {
		return tableManager;
	}
	
	public <T> void proccessBean(T bean){
		this.awareGeneratorFactoryAware(bean)
			.awareGeneratorConfigAware(bean);
	}
	
	public <T> DefaultGeneratorFactory awareGeneratorFactoryAware(T bean){
		if(bean instanceof GeneratorFactoryAware){
			GeneratorFactoryAware awareBean = (GeneratorFactoryAware) bean;
			awareBean.setGeneratorFactory(this);
		}
		return this;
	}
	
	public <T> DefaultGeneratorFactory awareGeneratorConfigAware(T bean){
		if(bean instanceof GeneratorConfigAware){
			GeneratorConfigAware awareBean = (GeneratorConfigAware) bean;
			awareBean.setGeneratorConfig(this.getConfig());
		}
		return this;
	}
	
	public <T> T createComponent(Class<T> clazz){
		return beanFactory.getBean(clazz, false);
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

}
