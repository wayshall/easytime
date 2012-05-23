package org.onetwo.core.weak.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.onetwo.core.util.NonCaseMap;

@SuppressWarnings("unchecked")
public interface ResultSetMapper {
	public NonCaseMap map(ResultSet rs, NonCaseMap map)throws SQLException ;
}
