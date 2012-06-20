package org.onetwo.core.codegenerator.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.onetwo.core.codegenerator.GeneratorConfig;
import org.onetwo.core.codegenerator.GeneratorFactory;
import org.onetwo.core.codegenerator.PrimaryKeyFactory;
import org.onetwo.core.codegenerator.db.meta.PrimaryKey;
import org.onetwo.core.codegenerator.db.meta.TableInfo;


@SuppressWarnings("unchecked")
public class DefaultPrimaryKeyFactory implements PrimaryKeyFactory, GeneratorFactoryAware{

	protected GeneratorFactory factory;
	
	@Override
	public void createPrimaryKey(ResultSet rs, TableInfo table) throws SQLException {
		PrimaryKey pk = null;
		boolean first = true;
		while (rs.next()) {
			if (first) {
				pk = (PrimaryKey) factory.createComponent(factory.getConfig().getClass(GeneratorConfig.COMPONENT_PRIMARY));
				pk = buildPrimaryKey(rs, pk);
				first = false;
			}
			String columnName = rs.getString("COLUMN_NAME");
			pk.addColumn(table.getColumn(columnName));
		}
		table.setPrimaryKey(pk);
	}
	protected PrimaryKey buildPrimaryKey(ResultSet rs, PrimaryKey pk) throws SQLException{
//		pk.setName(rs.getString("PK_NAME"));
		pk.setName(rs.getString("COLUMN_NAME"));
		return pk;
	}
	
	@Override
	public void setGeneratorFactory(GeneratorFactory factory) {
		this.factory = factory;
	}
	
}
