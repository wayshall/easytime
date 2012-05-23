package org.onetwo.core.weak.db.query;


@SuppressWarnings("unchecked")
public interface HqlSymbolParser {

	public static enum Keys {
		Empty,
		Null
	}
	
	public String parse(String field, Object value, Object paramValues);

}
