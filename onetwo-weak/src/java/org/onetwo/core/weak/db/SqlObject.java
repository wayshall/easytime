package org.onetwo.core.weak.db;

public interface SqlObject {
	public String toPreparedStatementSQL();
	public String toSQL();
	public Object[] getValues();
}
