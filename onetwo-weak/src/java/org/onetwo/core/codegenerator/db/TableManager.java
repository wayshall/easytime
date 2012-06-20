package org.onetwo.core.codegenerator.db;

import java.util.List;

import org.onetwo.core.codegenerator.db.meta.TableInfo;

public interface TableManager {

//	public String getDatabaseName();
	public TableInfo getTable(String tableName);
	public List<TableInfo> getTables(List<String> tnames);

	public TableInfo createTable(String tableName);

	public List<TableInfo> getTables();
	
	public List<String> getTableNames(boolean refrech);

}