package org.onetwo.core.weak.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ognl.Ognl;

import org.onetwo.core.logger.Logger;
import org.onetwo.core.weak.exception.db.DBException;


/**
 * @author zeng
 *
 */
public class WeakQuery implements Query {
	private static final Logger log = Logger.getLogger(WeakQuery.class);

	protected WeakSQL weakSql;

	protected Object[] values;
	
	protected List<Object[]> batchValues = new ArrayList<Object[]>();
	
	public WeakQuery(WeakSQL weakSql) {
		this.weakSql = weakSql;
		this.values = new Object[weakSql.getParamCount()];
	}
	
	public WeakQuery(String dbsql) {
		this(new WeakSQL(dbsql));
	}

	public Query setParameter(String name, Object value) {
		this.checkIfParamsExsit();
		Integer[] indexArray = (Integer[]) weakSql.getParams().get(name);
		if(indexArray==null || indexArray.length<1)
			throw new DBException("sql[" + weakSql.getDbql() + "] has not the parameter ["+name+"]");
		for(int i=0; i<indexArray.length; i++){
			this.values[indexArray[i].intValue()] = value;
			log.info("Query parameter " + (indexArray[i].intValue()+1) + " " +name +": " + value);
		}
		return this;
	}
	
	private void checkIfParamsExsit(){
		if (weakSql.getDbql().indexOf(VAR_NAMED_MARK) == -1 || weakSql.getParams().isEmpty()) {
			throw new DBException("sql[" + weakSql.getDbql() + "] has any parameter");
		}
	}
	
	public Query setParameterByBean(Object bean){
		this.checkIfParamsExsit();
		Set<Map.Entry<String, Object>> paramsSet = weakSql.getParams().entrySet();
		for(Map.Entry<String, Object> param : paramsSet){
			String name = param.getKey();
			try {
				Object value = Ognl.getValue(name, bean);
				this.setParameter(name, value);
			} catch (DBException e) {
				throw e;
			}  catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return this;
	}
	
	/**
	 */
	public Query setParameter(int index, Object value) {
		checkIfParamsExsit();
		this.values[index-1] = value;
		log.info("Query parameter " + index + " : " + value);
		return this;
	}

	/**
	 * 
	 */
	public String toSQL(){
		if(this.weakSql.getParamCount()<1)
			return this.weakSql.getDbql();
		StringBuffer sql = new StringBuffer(weakSql.getSql());
		int start = 0;
		String var = Query.VAR_QUESTION_MARK;
		for(int i=0; i<values.length; i++){
			start = sql.indexOf(var, start);
			sql.delete(start, start+var.length());
			String value = DBUtils.getValueSQLString(this.values[i]);
			sql.insert(start, value);
		}
		log.info("Query SQL : ["+sql+"]");
		
		return sql.toString();
	}
	
	public void addParameter() {
		this.batchValues.add(this.values);
	}

	public void addParameter(Object[] values) {
		this.batchValues.add(values);
	}
	
	public int batchSize(){
		return this.batchValues.size();
	}
	
	public String toPreparedStatementSQL(){
		return weakSql.getSql();
	}
	
	public void setValues(Object[] values) {
		this.values = values;
	}

	public Object[] getValues() {
		return values;
	}

	/**
	 * @return the batchValues
	 */
	public List<Object[]> getBatchValues() {
		return batchValues;
	}
	
	/**
	 * @param batchValues the batchValues to set
	 */
	public void setBatchValues(List<Object[]> batchValues) {
		this.batchValues = batchValues;
	}
	
	public static void main(String[] args) throws Exception{
		String str = "select * from bbs where id=? and li_address=$li_address and name=$aa.name and title=$title and name=$name";
		/*CZinfoVo cz = new CZinfoVo();
		cz.setLi_address("address");
		Query q = new WeakQuery(str)
			.setParameter(1, "test1")
			.setParameter("aa.name", "test_aa.name")
			.setParameter("name", "testname")
			.setParameter("title", "test3")
			.setParameterByBean(cz);
		System.out.println(q.toSQL());
		System.out.println("id: " + BeanUtils.getProperty(cz, "id"));*/
	}
}
