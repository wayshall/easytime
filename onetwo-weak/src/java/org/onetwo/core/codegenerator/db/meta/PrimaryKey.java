package org.onetwo.core.codegenerator.db.meta;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.core.codegenerator.Propertable;
import org.onetwo.core.codegenerator.util.GenerateUtils;

@SuppressWarnings("unchecked")
public class PrimaryKey extends Propertable{
	
	private String name;
	
	private List<ColumnInfo> columns = new ArrayList<ColumnInfo>();

	public PrimaryKey() {
	}
	public PrimaryKey(String name) {
		this.name = name;
	}
	
	public Class getJavaType(){
		Class type = null;
		if(this.columns.size()==1){
			type = this.columns.get(0).getJavaType();
		}else{
			type = super.getJavaType()==null?Long.class:super.getJavaType();
		}
		return type;
	}
	
	public boolean isGeneratorId(){
		return getJavaType()==Long.class;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.javaName = GenerateUtils.toPropertyName(name);
	}

	public List<ColumnInfo> getColumns() {
		return columns;
	}

	public ColumnInfo getColumn(String name) {
		for(ColumnInfo col : this.columns){
			if(col.getName().equalsIgnoreCase(name))
				return col;
		}
		return null;
	}

	public void setColumns(List<ColumnInfo> columns) {
		this.columns = columns;
	}

	public PrimaryKey addColumn(ColumnInfo column) {
		column.setPrimaryKey(true);
		this.columns.add(column);
		return this;
	}

	public PrimaryKey addColumn(String columnName, int sqlType) {
		ColumnInfo column = new ColumnInfo(columnName, sqlType);
		addColumn(column);
		return this;
	}
	
	public boolean isCompositeId(){
		return this.columns!=null && this.columns.size()>1;
	}

}
