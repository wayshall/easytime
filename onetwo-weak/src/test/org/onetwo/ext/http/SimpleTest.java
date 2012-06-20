package org.onetwo.ext.http;

import org.junit.Test;
import org.onetwo.common.http.HttpRequest;
import org.onetwo.common.http.HttpResponse;
import org.onetwo.common.http.URLFetch;
import org.onetwo.common.http.URLFetchFactory;
import org.onetwo.ext.xml.SimpleXmlObjectParser;

public class SimpleTest {

	@Test
	public void test2(){
		URLFetch fetch = URLFetchFactory.getURLFetch();
		HttpRequest request = new HttpRequest("http://www.javaeye.com/");
		HttpResponse response = fetch.fetch(request);
		System.out.println("text: " + response.asString());
	}
	
//	@Test
	public void test(){
		URLFetch fetch = URLFetchFactory.getURLFetch();
		HttpRequest request = new HttpRequest("http://twitter.com/account/verify_credentials.xml");
		request.setProxy("127.0.0.1", 9666);
		request.setAuth("wayshall", "iso900900");
		HttpResponse response = fetch.fetch(request);
		System.out.println("text: " + response.asString());
	}
	
//	@Test
	public void testTwitterVerify(){
		URLFetch fetch = URLFetchFactory.getURLFetch();
		HttpRequest request = new HttpRequest("http://twitter.com/account/verify_credentials.xml");
		request.setProxy("127.0.0.1", 9666);
		request.setAuth("wayshall", "iso900900");
		HttpResponse response = fetch.fetch(request);
		
		SimpleXmlObjectParser parser = new SimpleXmlObjectParser();
		parser.setContext(ConstantUtils.getOgnlContext());
		parser.map("user", User.class, false);
		parser.map("status", Status.class, false); 
		Object root = response.asObject(parser);
		System.out.println("text: " + response.asString());
		System.out.println("root: " + root);
		
	}

}
