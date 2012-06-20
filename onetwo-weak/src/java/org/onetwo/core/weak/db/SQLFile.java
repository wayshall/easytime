package org.onetwo.core.weak.db;

import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.onetwo.core.exception.ServiceException;
import org.onetwo.core.util.StringUtils;
import org.onetwo.core.weak.db.query.AnotherQuery;
import org.onetwo.core.weak.db.query.AnotherQueryImpl;

public class SQLFile {

	@Resource
	protected Properties sqlfile;
	
	public SQLFile(){
	}
	
	public AnotherQuery createQuery(String queryName){
		String sql = sqlfile.getProperty(queryName);
		if(StringUtils.isBlank(sql))
			throw new ServiceException("can not find the query : " + queryName);
		
		AnotherQuery query = new AnotherQueryImpl(sql);
		return query;
	}
	
	public AnotherQuery createQuery(String queryName, Object...objects){
		AnotherQuery query = this.createQuery(queryName);
		for(int i=0; i<objects.length; i++)
			query.setParameter(i, objects[i]);
		query.compile();
		
		return query;
	}
	
	public AnotherQuery createQuery(String queryName, Map<String, Object> params){
		AnotherQuery query = this.createQuery(queryName);
		for(Map.Entry<String, Object> p : params.entrySet())
			query.setParameter(p.getKey(), p.getValue());
		query.compile();
		
		return query;
	}

}
