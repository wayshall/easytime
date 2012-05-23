package org.onetwo.core.weak.db.query;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.onetwo.core.exception.ServiceException;

/***
 * SqlSymbolManager的抽象实现
 * @author weishao
 *
 */
abstract public class AbstractSqlSymbolManager implements SqlSymbolManager {

	public static final char SPLIT_SYMBOL = ':';
	
	protected Map<String, HqlSymbolParser> parser;

	public AbstractSqlSymbolManager() {
		parser = new HashMap<String, HqlSymbolParser>();
	}

	/***
	 * 注册所有查询接口所支持的操作符
	 * @return
	 */
	@PostConstruct
	public AbstractSqlSymbolManager initParser() {
		register("=", new CommonHqlSymbolParser("="))
		.register(">", new CommonHqlSymbolParser(">"))
		.register(">=", new CommonHqlSymbolParser(">="))
		.register("<", new CommonHqlSymbolParser("<"))
		.register("<=", new CommonHqlSymbolParser("<="))
		.register("!=", new CommonHqlSymbolParser("!="))
		.register("<>", new CommonHqlSymbolParser("<>"))
		.register("like", new CommonHqlSymbolParser("like"))
		.register("not like", new CommonHqlSymbolParser("not like"))
		.register("in", new InSymbolParser());
		return this;
	}

	public HqlSymbolParser getHqlSymbolParser(String symbol) {
		HqlSymbolParser parser = this.parser.get(symbol);
		if (parser == null)
			throw new ServiceException("do not support symbol : " + symbol);
		return parser;
	}

	/****
	 * 注册操作符和对应的解释类
	 */
	public AbstractSqlSymbolManager register(String symbol, HqlSymbolParser parser) {
		this.parser.put(symbol, parser);
		return this;
	}

}
