package org.onetwo.core.weak.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.onetwo.core.exception.ServiceException;
import org.onetwo.core.util.MyUtils;
import org.onetwo.core.weak.db.query.AnotherQuery;
import org.onetwo.core.weak.db.query.AnotherQueryFactory;

@SuppressWarnings("unchecked")
public class DBConnecton {
	
	private Connection connection;
	private PreparedStatement preparedStatement;
	
	public DBConnecton(Connection connection){
		this.connection = connection;
	}
	
	public ResultSet query(String sql, Object...objects){
		return query(sql, MyUtils.convertParamMap(objects));
	}
	
	public ResultSet query(String sql, Map params){
		return (ResultSet) execute(sql, params, true, false);
	}

	
	public Object unique(String sql, Map params){
		Map row = one(sql, params);
		Collection rs = row.values();
		if(rs==null || rs.isEmpty())
			return null;
		return rs.iterator().next();
	}
	
	public Map one(String sql, Object...objects){
		return one(sql, MyUtils.convertParamMap(objects));
	}
	
	public Map one(String sql, Map params){
		List<Map> datas = queryForList(sql, params);
		if(datas==null || datas.isEmpty())
			return null;
		Map row = datas.get(0);
		return row;
	}
	
	public List<Map> queryForList(String sql, Map params){
		try {

			ResultSet rs = query(sql, params);

			List<Map> datas = new ArrayList<Map>();
			datas = DBUtils.toList(rs, true);
			
			return datas;

		}finally{
			closePreparedStatement();
		}
	}
	
	public int update(String sql, Map params){
		try{
			return (Integer) execute(sql, params, false);
		}finally{
			closePreparedStatement();
		}
	}

	
	public Object execute(String sql, Map params, boolean isQuery){
		return execute(sql, params, isQuery, false);
	}
	
	public Object execute(String sql, Map params, boolean isQuery, boolean autoClosePstm){
		AnotherQuery q = AnotherQueryFactory.create(sql);
		if(params!=null && !params.isEmpty())
			q.setParameters(params);
		q.compile();
		this.preparedStatement = createPreparedStatement(q.getTransitionSql());
		int index = 0;
		for(Object value : q.getValues()){
			DBUtils.setPstmParameter(preparedStatement, index+1, value);
			index++;
		}
		try {
			if(isQuery)
				return preparedStatement.executeQuery();
			else
				return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new ServiceException("execute sql error : " + sql, e);
		} finally{
			if(autoClosePstm)
				closePreparedStatement();
		}
	}

	protected PreparedStatement createPreparedStatement(String sql){
		try {
			PreparedStatement pstm = this.connection.prepareStatement(sql);
			return pstm;
		} catch (SQLException e) {
			throw new ServiceException("createPreparedStatement error : " + sql, e);
		}
	}
	
	public void close(){
		DBUtils.close(connection, preparedStatement, null);
	}
	
	public void closePreparedStatement(){
		DBUtils.closePreparedStatement(this.preparedStatement);
	}
}
