package org.onetwo.core.db.test;

import junit.framework.TestCase;

import org.onetwo.core.weak.db.query.AnotherQuery;
import org.onetwo.core.weak.db.query.AnotherQueryImpl;

public class AnotherQueryTest extends TestCase{

	public void testSimple(){
		String str = "select * from bbs where id>? and li_address >= :li_address and name=:aa.name and title=:title and name = :name";
		AnotherQuery q = new AnotherQueryImpl(str);
		
		Long id = 1l;
		String address = "address";
		String name = "name.value";
		String aa_name = "aa.name.value";
		q.setParameter(0, id).setParameter("li_address", address).setParameter("aa.name", aa_name).setParameter(4, name).compile();
		
		assertEquals(q.getValues().get(0), id);
		assertEquals(q.getValues().get(1), address);
		assertEquals(q.getValues().get(2), aa_name);
		assertEquals(q.getValues().get(3), name);
		assertEquals(q.getTransitionSql(), "select * from bbs where id > ? and li_address >= ? and name = ? and 1=1 and name = ?");
	}

	public void testSameName(){
		String str = "select * from bbs where id>? and li_address >= :li_address and (aa_name=:name or name = :name) and title=:title";
		AnotherQuery q = new AnotherQueryImpl(str);
		
		Long id = 1l;
		String address = "address";
		String name = "name.value";
		q.setParameter(0, id).setParameter("li_address", address).setParameter("name", name).compile();
		
		assertEquals(q.getValues().get(0), id);
		assertEquals(q.getValues().get(1), address);
		assertEquals(q.getValues().get(2), name);
		assertEquals(q.getValues().get(3), name);
		assertEquals(q.getTransitionSql(), "select * from bbs where id > ? and li_address >= ? and (aa_name = ? or name = ?) and 1=1");
	}
}
