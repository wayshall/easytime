package org.onetwo.core.weak.db;

import java.util.List;

public interface Query extends  SqlObject{

	public static final String VAR_QUESTION_MARK = "?";
	public static final String VAR_NAMED_MARK = "$";
	
	public Query setParameter(String name, Object value);

	public Query setParameter(int index, Object value);
	
	public Query setParameterByBean(Object bean);
	
	public void addParameter();
	
	public void addParameter(Object[] values);
	
	public List<Object[]> getBatchValues();
	
	public void setBatchValues(List<Object[]> batchValues);

}