package org.onetwo.ext.xml;

import java.io.File;
import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.onetwo.core.exception.BaseException;
import org.onetwo.core.util.Assert;

abstract public class DomUtils {
	
	public static Document readXml(File file){
		Assert.notNull(file);
		SAXReader reader = createSAXReader();
		Document document;
		try {
			document = reader.read(file);
		} catch (DocumentException e) {
			throw new BaseException("read xml error : " + file.getPath(), e);
		}
		return document;
	}
	
	public static Document readXml(InputStream in){
		Assert.notNull(in);
		SAXReader reader = createSAXReader();
		Document document;
		try {
			document = reader.read(in);
		} catch (DocumentException e) {
			throw new BaseException("read xml error : " + in, e);
		}
		return document;
	}
	
	public static SAXReader createSAXReader(){
		SAXReader reader = new SAXReader();
		return reader;
	}

}
