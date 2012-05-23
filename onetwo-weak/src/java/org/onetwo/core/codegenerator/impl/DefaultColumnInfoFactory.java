package org.onetwo.core.codegenerator.impl;

import java.sql.SQLException;
import java.util.List;

import org.onetwo.core.codegenerator.ColumnInfoFactory;
import org.onetwo.core.codegenerator.db.meta.ColumnInfo;
import org.onetwo.core.codegenerator.db.meta.TableInfo;
import org.onetwo.core.util.BaseMap;


@SuppressWarnings("unchecked")
public class DefaultColumnInfoFactory implements ColumnInfoFactory{

	@Override
	public void createColumnInfo(List<BaseMap> rs, TableInfo table) throws SQLException {
		ColumnInfo column = null;
		for(BaseMap row : rs){
			column = newColumn(row);
			table.addColumn(column);
		}
	}
	
	protected ColumnInfo newColumn(BaseMap rs) throws SQLException{
		
		String name = (String)rs.get("COLUMN_NAME");
		int sqlType = rs.getInteger("DATA_TYPE");
		String comment = rs.getString("REMARKS");
		ColumnInfo column = new ColumnInfo(name, sqlType);
		column.setComment(comment);
		return column;
	}
	
}
