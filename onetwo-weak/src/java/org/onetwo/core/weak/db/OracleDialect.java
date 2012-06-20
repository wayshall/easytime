package org.onetwo.core.weak.db;


/***
 * 扩展hibernate的oracle方言
 * 
 * @author weishao
 *
 */
public class OracleDialect  {
	

	public String getLimitString(String sql, boolean hasOffset) {
		return getOracleLimitString(sql, hasOffset);
	}

	public static String getOracleLimitString(String sql, boolean hasOffset) {
		sql = sql.trim();
		boolean isForUpdate = false;
		if ( sql.toLowerCase().endsWith(" for update") ) {
			sql = sql.substring( 0, sql.length()-11 );
			isForUpdate = true;
		}

		StringBuffer pagingSelect = new StringBuffer( sql.length()+100 );
		if (hasOffset) {
			pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
		}
		else {
			pagingSelect.append("select * from ( ");
		}
		pagingSelect.append(sql);
		
		boolean isOrderBy = sql.indexOf(" order by ")!=-1 && sql.indexOf("group by")==-1;
		if(isOrderBy)
			pagingSelect.append(", rownum");
		
		if (hasOffset) {
			pagingSelect.append(" ) row_ where rownum <= ?) where rownum_ > ?");
		}
		else {
			pagingSelect.append(" ) where rownum <= ?");
		}

		if ( isForUpdate ) {
			pagingSelect.append( " for update" );
		}

		return pagingSelect.toString();
	}

}
