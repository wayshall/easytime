package org.onetwo.core.weak.db.query;


@SuppressWarnings("unchecked")
abstract public class AnotherQueryFactory {

	public static AnotherQuery create(String sql){
		return new AnotherQueryImpl(sql);
	}
}