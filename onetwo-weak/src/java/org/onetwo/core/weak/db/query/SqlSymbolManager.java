package org.onetwo.core.weak.db.query;

import java.util.List;
import java.util.Map;

/***
 * sql操作符管理
 * 
 * @author weishao
 *
 */
public interface SqlSymbolManager {
	
	public AbstractSqlSymbolManager register(String symbol, HqlSymbolParser parser);
	
	public HqlSymbolParser getHqlSymbolParser(String symbol);
	
	public String createHql(Map properties, List<Object> values) ;
	
}
