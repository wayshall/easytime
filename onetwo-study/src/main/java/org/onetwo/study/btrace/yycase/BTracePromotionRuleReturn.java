package org.onetwo.study.btrace.yycase;

import static com.sun.btrace.BTraceUtils.println;

import java.util.Optional;

import com.sun.btrace.annotations.BTrace;
import com.sun.btrace.annotations.Kind;
import com.sun.btrace.annotations.Location;
import com.sun.btrace.annotations.OnMethod;
import com.sun.btrace.annotations.Return;
import com.sun.btrace.annotations.Self;

@BTrace
public class BTracePromotionRuleReturn {
	@OnMethod(clazz = "PromotionRuleContext", method = "simpleApplyRule", location = @Location(Kind.RETURN))
	public static void traceExecute(@Self Object instance, @Return Optional<?> ret) {
		println("call PromotionRuleContext.simpleApplyRule1");
		println("call PromotionRuleContext.simpleApplyRule2");
		// jstack();
	}
}
