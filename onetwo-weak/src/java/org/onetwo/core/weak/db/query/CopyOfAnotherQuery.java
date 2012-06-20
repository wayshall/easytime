package org.onetwo.core.weak.db.query;

import java.util.List;

@SuppressWarnings("unchecked")
public interface CopyOfAnotherQuery {

	public void compile();

	public CopyOfAnotherQuery setParameter(int index, Object value);

	public CopyOfAnotherQuery setParameter(String varname, Object value);

	public int getParameterCount();

	public int getActualParameterCount();

	public List getValues();

	public String getTransitionSql();

}