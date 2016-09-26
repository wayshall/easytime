package org.onetwo.study.btrace.yycase;

import static com.sun.btrace.BTraceUtils.println;

import com.sun.btrace.AnyType;
import com.sun.btrace.annotations.BTrace;
import com.sun.btrace.annotations.Kind;
import com.sun.btrace.annotations.Location;
import com.sun.btrace.annotations.OnMethod;
import com.sun.btrace.annotations.Self;

@BTrace
public class TraceRestTemplateArgs {
	@OnMethod(clazz = "org.springframework.web.client.RestTemplate", method = "exchange", location = @Location(Kind.ENTRY))
	public static void traceExecute(@Self Object instance, String url, AnyType method, AnyType requestEntity,
			AnyType responseType, AnyType uriVariables) {
		println("url is:" + url);
		println("uriVariables is:" + uriVariables);
		println("call RestTemplate.exchange");
		// jstack();
	}
}
