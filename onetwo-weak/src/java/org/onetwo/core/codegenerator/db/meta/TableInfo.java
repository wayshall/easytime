package org.onetwo.core.codegenerator.db.meta;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.onetwo.core.codegenerator.util.GenerateUtils;
import org.onetwo.core.util.StringUtils;

public class TableInfo {

	private String name;
	
	private String prefix; 

	private String className;

	private PrimaryKey primaryKey;

	private Map<String, ColumnInfo> columns = new LinkedHashMap<String, ColumnInfo>();
	
	private String comment;

	public TableInfo(String name, String prefix) {
		this.name = name;
		setPrefix(prefix);
	}
	
	public TableInfo(String className){
		this.className = className;
	}

	public String getName() {
		return this.name;
	}

	public Map<String, ColumnInfo> getColumns() {
		return columns;
	}

	public Collection<ColumnInfo> getColumnCollection() {
		return columns.values();
	}

	public ColumnInfo getColumn(String name) {
		return columns.get(name);
	}

	public void setColumns(Map<String, ColumnInfo> columns) {
		this.columns = columns;
	}

	public TableInfo addColumn(ColumnInfo column) {
		this.columns.put(column.getName(), column);
		return this;
	}

	public TableInfo addColumn(String name, int sqlType) {
		ColumnInfo column = new ColumnInfo(name, sqlType);
		return addColumn(column);
	}

	public PrimaryKey getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(PrimaryKey primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getClassName() {
		if(StringUtils.isNotBlank(className))
			return className;
		return GenerateUtils.toClassName(this.name.substring(prefix.length()));
	}

	public String getPrefix() {
		return prefix;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
