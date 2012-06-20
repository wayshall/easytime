package org.onetwo.core.codegenerator;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.onetwo.core.codegenerator.db.meta.TableInfo;

public interface PrimaryKeyFactory {
	
	public void createPrimaryKey(ResultSet rs, TableInfo table) throws SQLException; 

}
