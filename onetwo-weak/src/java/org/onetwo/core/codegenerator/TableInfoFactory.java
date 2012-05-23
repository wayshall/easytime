package org.onetwo.core.codegenerator;

import java.util.Map;

import org.onetwo.core.codegenerator.db.meta.TableInfo;

public interface TableInfoFactory {
	
	public TableInfo createTableInfo(Map<String, Object> rs); 

}
