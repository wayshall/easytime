package org.onetwo.study.btrace.yycase;

import static com.sun.btrace.BTraceUtils.println;

import java.util.Collection;

import com.sun.btrace.annotations.BTrace;
import com.sun.btrace.annotations.Kind;
import com.sun.btrace.annotations.Location;
import com.sun.btrace.annotations.OnMethod;
import com.sun.btrace.annotations.Self;

@BTrace
public class BTraceRouterArgs {
	@OnMethod(clazz = "test.RouterServiceImpl", method = "getSalableSkus", location = @Location(Kind.ENTRY))
	public static void traceExecute(@Self Object instance, String sid, Collection<Long> skuIds) {
		println("call RouterServiceImpl.getSalableSkus");
		println("skuIds is:" + skuIds);
		println("sid is:" + sid);
		// jstack();
	}
}
