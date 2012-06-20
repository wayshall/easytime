package org.onetwo.core.weak.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

public class ResultSetBean {
	
	private Object[][] data;
	private Properties column;
	private int columnCount;
	private int rowCount;
	
	public ResultSetBean(){
		this.columnCount = 0;
		this.rowCount = 0;
		data = new Object[this.rowCount][this.columnCount];
		column = new Properties();
	}
	
	/**
	 * after create instance of ResultSetBean, must invoke this method
	 * @param rs
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ResultSetBean setToData(ResultSet rs) throws SQLException{
		if(rs==null){
			return this;
		}
		ResultSetMetaData rsMeta = rs.getMetaData();

		int colCount = rsMeta.getColumnCount();
		for(int i=1; i<=colCount; i++){
			if(i==1)
				System.out.println(rsMeta.getColumnName(i));
			column.setProperty(rsMeta.getColumnName(i).toLowerCase(), String.valueOf(i-1));
		}
		Object[] rowData = null;
		List rsList = new ArrayList();
		while(rs.next()){
			rowData = new Object[colCount];
			for(int col=1; col<=colCount; col++){
				rowData[col-1] = this.processResult(rs, rsMeta, col);
			}
			rsList.add(rowData);
		}

		this.rowCount = rsList.size();
		this.columnCount = rsMeta.getColumnCount();
		if(rsList.isEmpty())
			return this;
		data = new Object[this.rowCount][this.columnCount];
		for(int row=0; row<rsList.size(); row++){
			data [row] = (Object[])rsList.get(row);
		}
		return this;
	}
	
	/**
	 * subClass can extend
	 * @param rs
	 * @param rsMeta
	 * @param col
	 * @return
	 * @throws SQLException
	 */
	protected Object processResult(ResultSet rs, ResultSetMetaData rsMeta, int col) throws SQLException{
		Object value = null;
		//System.out.println("----------->>>> ColumnTypeName: "+ rsMeta.getColumnTypeName(col));
		int sqlType = rsMeta.getColumnType(col);
		if(sqlType == Types.VARCHAR){
			value = rs.getString(col);
		}
		else if(sqlType == Types.INTEGER){
			value = rs.getInt(col);
		}
		else if(sqlType == Types.FLOAT){
			value = rs.getFloat(col);
		}
		else if(sqlType == Types.BOOLEAN){
			value = rs.getBoolean(col);
		}
		else if(sqlType == Types.DOUBLE){
			value = rs.getDouble(col);
		}
		else if(sqlType == Types.DATE){
			value = rs.getDate(col);
		}
		else if(sqlType == Types.TIME){
			value = rs.getTime(col);
		}
		else if(sqlType == Types.TIMESTAMP){
			value = rs.getTimestamp(col);
		}
		else if(sqlType==Types.DECIMAL || sqlType==Types.NUMERIC){
			value = rs.getBigDecimal(col);
		}
		else{
			value = rs.getObject(col);
		}
		return value;
	}
	
	public Object getObject(int row, int col){
		if(row<0 || row>this.rowCount){
			throw new RuntimeException("index error ["+row+"]["+this.rowCount+"]");
			//return null;
		}
		if(col<0 || row>this.rowCount){
			throw new RuntimeException("index error ["+row+"]["+this.rowCount+"]");
			//return null;
		}
		return this.data[row][col];
	}
	
	public Object getObject(int row, String colName){
		String colStr = column.getProperty(colName.toLowerCase(), "-1");
		int col = Integer.parseInt(colStr);
		if(col==-1)
			throw new RuntimeException("ndex error ["+colName+"]");
		else
			return this.getObject(row, col);
	}
	
	public String getString(int row, int col){
		Object value = this.getObject(row, col);
		if(value==null)
			return null;
		else
			return value.toString();
	}
	
	public String getString(int row, String colName){
		Object value = this.getObject(row, colName);
		if(value==null)
			return null;
		else
			return value.toString();
	}
	
	public int getColumnCount() {
		return columnCount;
	}

	public int getRowCount() {
		return rowCount;
	}
	
	/**
	 * @param col
	 * @return
	 */
	public String getColumnName(int col){
		if(this.column==null || this.column.isEmpty())
			return "";
		Collection list = this.column.values();
		if(col>list.size())
			return "";
		Enumeration enu = this.column.propertyNames();
		String columnName = "";
		while(enu.hasMoreElements()){
			String key = (String)enu.nextElement();
			String value = this.column.getProperty(key, "-1");
			if(Integer.parseInt(value)==col){
				columnName = key;
				break;
			}
		}
		return columnName;
	}
	
	/**
	 * @param columnName
	 * @return
	 */
	public int getColumnIndex(String columnName){
		return Integer.parseInt(this.column.getProperty(columnName.toLowerCase(), "-1"));
	}
	
	
}
