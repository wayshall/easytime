package org.onetwo.ext.parametied;

public class Model<T> {
	private static final String fmt = "%24s: %s%n";

	public Model() {

	}
	

	public <K> Object getOutName2(K name) {
		return null;
	}
	public Object getOutName3(T name) {
		return null;
	}

	public T getOutName(T name) {

		System.out.println(name);
		return null;
	}

	public String getStr(String str) {
		return str;
	}
}
