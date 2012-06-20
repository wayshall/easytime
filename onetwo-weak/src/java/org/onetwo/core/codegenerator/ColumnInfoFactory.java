package org.onetwo.core.codegenerator;

import java.sql.SQLException;
import java.util.List;

import org.onetwo.core.codegenerator.db.meta.TableInfo;
import org.onetwo.core.util.BaseMap;

public interface ColumnInfoFactory {
	
	public void createColumnInfo(List<BaseMap> rs,TableInfo table) throws SQLException; 

}
