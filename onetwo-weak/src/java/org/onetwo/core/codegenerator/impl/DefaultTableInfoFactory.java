package org.onetwo.core.codegenerator.impl;

import java.sql.SQLException;
import java.util.Map;

import org.onetwo.core.codegenerator.GeneratorConfig;
import org.onetwo.core.codegenerator.TableInfoFactory;
import org.onetwo.core.codegenerator.db.meta.TableInfo;
import org.onetwo.core.exception.ServiceException;
import org.onetwo.core.util.ReflectUtils;

@SuppressWarnings("unchecked")
public class DefaultTableInfoFactory implements TableInfoFactory, GeneratorConfigAware {

	protected GeneratorConfig config;

	public TableInfo createTableInfo(Map<String, Object> rs){
		TableInfo table = null;
		try {
			if (rs!=null) {
				table = newTable(rs);
			}
		} catch (SQLException e) {
			throw new ServiceException(e);
		}
		return table;
	}

	protected TableInfo newTable(Map<String, Object> rs) throws SQLException {
		String tname = (String)rs.get("TABLE_NAME");
		Class clazz = config.getClass(GeneratorConfig.COMPONENT_TABLEINFO, TableInfo.class);
		TableInfo table = (TableInfo)ReflectUtils.newInstance(clazz, tname, config.getTablePrefix());//new TableInfo(tname, config.getTablePrefix());
		String comment = (String)rs.get("REMARKS");
		table.setComment(comment);
		return table;
	}

	public void setGeneratorConfig(GeneratorConfig config) {
		this.config = config;
	}

	
}
