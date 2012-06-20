package org.onetwo.core.codegenerator.db.meta;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import org.onetwo.core.codegenerator.Propertable;
import org.onetwo.core.codegenerator.db.TypeMapper;
import org.onetwo.core.codegenerator.util.GenerateUtils;

@SuppressWarnings("unchecked")
public class ColumnInfo extends Propertable {
	
	protected String name;
	protected int sqlType; 
	protected boolean primaryKey;
	
	private String comment;
	
	public ColumnInfo(){
	}

	public ColumnInfo(String name, int sqlType) {
		this.name = name;
		this.javaName = GenerateUtils.toPropertyName(name);
		this.sqlType = sqlType;
		this.javaType = TypeMapper.getJavaType(sqlType); 
	}

	public String getName() {
		return name;
	}

	public int getSqlType() {
		return sqlType;
	}
	
	public boolean isDateType(){
		return getJavaType()==Date.class || getJavaType()==Time.class || getJavaType()==Timestamp.class;
	}

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ColumnInfo other = (ColumnInfo) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
