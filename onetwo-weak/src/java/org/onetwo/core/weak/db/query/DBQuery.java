package org.onetwo.core.weak.db.query;

import java.util.List;
import java.util.Map;

import org.onetwo.core.exception.ServiceException;
import org.onetwo.core.weak.db.DBConnecton;

@SuppressWarnings("unchecked")
public class DBQuery {
	
	private DBConnecton dbcon;
	
	
	public DBQuery(DBConnecton dbcon){
		this.dbcon = dbcon;
	}
	
	public Object unique(String sql, Map params){
		AnotherQuery q = AnotherQueryFactory.create(sql);
		q.setParameters(params);
		q.compile();
		Object datas = null;
		try {
			datas = dbcon.unique(sql, params);
		} catch (Exception e) {
			handleException(e);
		}finally{
			this.dbcon.close();
		}
		return datas;
	}
	
	public List<Map> queryForMap(String sql, Map params){
		AnotherQuery q = AnotherQueryFactory.create(sql);
		q.setParameters(params);
		q.compile();
		List<Map> datas = null;
		try {
			datas = dbcon.queryForList(sql, params);
		} catch (Exception e) {
			handleException(e);
		}finally{
			this.dbcon.close();
		}
		return datas;
	}
	
	protected void handleException(Exception e){
		if(e instanceof ServiceException)
			throw (ServiceException)e;
		else
			throw new ServiceException(e);
	}

}
