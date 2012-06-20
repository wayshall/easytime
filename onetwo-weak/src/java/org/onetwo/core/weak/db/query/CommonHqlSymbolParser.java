package org.onetwo.core.weak.db.query;

import java.util.List;
import java.util.Map;

import org.onetwo.core.exception.ServiceException;
import org.onetwo.core.util.MyUtils;
import org.onetwo.core.util.StringUtils;


/***
 * 可用于解释一般的操作符，如=,<,> ……
 * @author weishao
 *
 */
@SuppressWarnings("unchecked")
public class CommonHqlSymbolParser implements HqlSymbolParser{
//	protected Logger logger = Logger.getLogger(this.getClass());
	
	public static final String LIKE = "like";
	
	protected String symbol;
	
	public CommonHqlSymbolParser(String symbol){
		this.symbol = symbol;
	}
	
	public String parse(String field, Object value, Object paramValues){
		if(value==null || (value instanceof String && StringUtils.isBlank(value.toString())))
			return null;
		
		List list = MyUtils.asList(value);
		if(list==null || list.isEmpty())
			return null;
		
		StringBuilder sb = new StringBuilder();
		boolean mutiValue = list.size()>1;
		Object v = null;
		if(mutiValue)
			sb.append("(");
		for(int i=0; i<list.size(); i++){
			v = list.get(i);

			if(isLike()){
				if(!(v instanceof String))
					throw new ServiceException("the symbol is [like], the value must a string type!");
				v = MyUtils.getLikeString(list.get(i).toString());
			}else if(Keys.Empty.equals(v)){
				v = "";
			}
			
			process(field, symbol, i, v, sb, paramValues);
			
			if(i!=list.size()-1)
				sb.append(" or ");
		}
		if(mutiValue)
			sb.append(") ");
		
		return sb.toString();
	}
	
	protected void process(String field, String symbol, int index, Object value, StringBuilder sqlScript, Object paramValues){
		if(value==Keys.Null){
			if("=".equals(symbol)){
				sqlScript.append(field).append(" is null ");
				return ;
			}
			if("!=".equals(symbol)){
				sqlScript.append(field).append(" is not null ");
				return ;
			}
		}
		if(paramValues instanceof List){
			List list = (List) paramValues;
			sqlScript.append(field).append(" ").append(symbol).append(" ? ");
			list.add(value);
		}
		else if(paramValues instanceof Map){
			Map map = (Map) paramValues;
			int size = map.entrySet().size();
			sqlScript.append(field).append(" ").append(symbol).append(" :").append(field).append(size).append(" ");
			map.put(field+size, value);
		}
		else{
			throw new ServiceException("the paramValues must be List or Map!");
		}
	}
	
	public String parse2(String field, Object value, List paramValues){
		if(value==null || (value instanceof String && StringUtils.isBlank(value.toString())))
			return null;
		
		List list = MyUtils.asList(value);
		if(list==null || list.isEmpty())
			return null;
		
		StringBuilder sb = new StringBuilder();
		boolean mutiValue = list.size()>1;
		if(mutiValue)
			sb.append("(");
		for(int i=0; i<list.size(); i++){
			sb.append(field).append(" ").append(symbol).append(" ? ");

			if(isLike()){
				if(!(value instanceof String))
					throw new ServiceException("the symbol is [like], the value must a string type!");
				paramValues.add(MyUtils.getLikeString(list.get(i).toString()));
			}else
				paramValues.add(list.get(i));
			
			if(i!=list.size()-1)
				sb.append(" or ");
		}
		if(mutiValue)
			sb.append(") ");
		
		return sb.toString();
	}
	
	public boolean isLike(){
		return LIKE.equalsIgnoreCase(symbol);
	}

}
