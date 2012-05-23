package org.onetwo.core.codegenerator;

import java.util.Properties;

import org.onetwo.core.beanfactory.BeanFactory;
import org.onetwo.core.codegenerator.db.DatabaseManager;
import org.onetwo.core.codegenerator.db.TableManager;
import org.onetwo.core.codegenerator.util.FreemarkerTemplate;

public interface GeneratorFactory {
	
	public void init(Properties dbconfig);
	
	public GeneratorConfig getConfig();
	 
	public DatabaseManager getDatabaseManager();
	
	public TableManager getTableManager();
	
	public FreemarkerTemplate getFreemarkerTemplate();
	
	public TableInfoFactory getTableInfoFactory();
	
	public ColumnInfoFactory getColumnInfoFactory();
	
	public PrimaryKeyFactory getPrimaryKeyFactory();
	
	public <T> T createComponent(Class<T> clazz);
	
	public BeanFactory getBeanFactory();
	
	public Object getContextBean();
}
