package org.onetwo.ext.utils;

import org.apache.commons.codec.binary.Base64;

public abstract class Base64Encoder {
	
	public static String encode(String value){
		byte[] rs = Base64.encodeBase64(value.getBytes());
		return new String(rs);
	}

}
