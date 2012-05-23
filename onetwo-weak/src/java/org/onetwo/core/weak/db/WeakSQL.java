package org.onetwo.core.weak.db;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.onetwo.core.logger.Logger;
import org.onetwo.core.util.ArrayUtils;

/**
 * @author wayshall
 *
 */
public class WeakSQL {
	protected Logger logger = Logger.getLogger(this.getClass());
	
	private static final Pattern PATTERN = Pattern.compile("(\\:[\\w\\.]+)|(\\?)", Pattern.CASE_INSENSITIVE);

	protected String dbql;

	protected Map<String, Object> params;
	
	protected int paramCount;
	
	protected String sql;
	
	public WeakSQL(String dbql){
		this.dbql = dbql;
		this.params = new LinkedHashMap<String, Object>();
		parseParameters();
	}
	
	private void parseParameters(){
		Matcher m = PATTERN.matcher(this.dbql);
		
		StringBuffer sql = new StringBuffer(this.dbql);
		int start = 0;
		
		while(m.find()){
			String varWithMark = m.group();
			System.out.println("var: " +varWithMark);
			if(varWithMark.startsWith(Query.VAR_NAMED_MARK)){
				String var = varWithMark.substring(Query.VAR_NAMED_MARK.length());
				Object indexObj = params.get(var);
				if(indexObj==null)
					indexObj = new Integer[0];
				Integer[] indexArray = (Integer[]) indexObj;
				indexArray = (Integer[]) ArrayUtils.add(indexArray, new Integer(paramCount));
				params.put(var, indexArray);
				
				//to sql
				start = sql.indexOf(varWithMark, start);
				sql.delete(start, start+varWithMark.length());
				sql.insert(start, "?");
			}
			paramCount++;
		}
		this.sql = sql.toString();
	}

	/**
	 * @return
	 */
	public String getSql() {
		return sql;
	}

	public String getDbql() {
		return dbql;
	}

	public void setDbql(String dbql) {
		this.dbql = dbql;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
	public int getParamCount() {
		return paramCount;
	}

	public static void main(String[] args){
		String str = "select * from bbs where id=? and li_address= :li_address and name=:aa.name and title=$title and name=#name";
		WeakSQL q = new WeakSQL(str);
		System.out.println(q.getSql());
		System.out.println(q.getParams());
	}
	
}
