package org.onetwo.ext.xml;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.core.util.StringUtils;

public class MenuConfig {
	
	/*public static enum DependencyType {
		any,
		project,
		java_project,
		file
	}*/
	
	public static final String any = "any";

	private String id;
	private String name;
	private String type;
	private String printinfo;
	private String implementor;
	private String dependency;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImplementor() {
		return implementor;
	}

	public String[] getStatments() {
//		String[] strs = implementor.replaceAll("\\r", " ").split(";");
		String[] strs = implementor.split(";");
		return strs;
	}

	public void setImplementor(String implementor) {
		this.implementor = implementor;
	}
	
	public boolean canPrintinfo(){
		return "true".equals(printinfo);
	}

	public String getPrintinfo() {
		return printinfo;
	}

	public void setPrintinfo(String printinfo) {
		this.printinfo = printinfo;
	}

	public String getDependency() {
		if(StringUtils.isBlank(dependency))
			dependency = any;
		return dependency;
	}

	public void setDependency(String dependency) {
		this.dependency = dependency;
	}
	
	public List<String> getDependencyTypes(){
		String[] typeStrs = StringUtils.split(getDependency(), ",");
		List<String> types = new ArrayList<String>();
		for(String tstr : typeStrs){
			types.add(tstr.trim());
		}
		return types;
	}
	
	public boolean isMatchDependency(String type){
		List<String> types = getDependencyTypes();
		if(types.contains(any))
			return true;
		if(type==null)
			return true;
		else
			return types.contains(type);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
