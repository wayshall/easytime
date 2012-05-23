package org.onetwo.core.codegenerator.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.onetwo.core.codegenerator.ColumnInfoFactory;
import org.onetwo.core.codegenerator.GeneratorConfig;
import org.onetwo.core.codegenerator.PrimaryKeyFactory;
import org.onetwo.core.codegenerator.TableInfoFactory;
import org.onetwo.core.codegenerator.db.meta.TableInfo;
import org.onetwo.core.codegenerator.impl.GeneratorConfigAware;
import org.onetwo.core.exception.ServiceException;
import org.onetwo.core.util.BaseMap;
import org.onetwo.core.util.MyUtils;
import org.onetwo.core.util.NonCaseMap;
import org.onetwo.core.weak.db.DBConnecton;
import org.onetwo.core.weak.db.DBUtils;
import org.onetwo.core.weak.db.ResultSetMapper;

@SuppressWarnings("unchecked")
public class DefaultTableManager implements TableManager, GeneratorConfigAware{
	
	public static class OracleSql {
		public static final String select_all_table = "SELECT TABLE_NAME FROM user_tables";
		public static final String select_column_comments = "SELECT t.column_name, t.comments FROM user_col_comments t where t.table_name = :tableName";
		public static final String select_table_comments = "SELECT * FROM user_tab_comments t where t.table_name = :tableName";
	}

	protected String catalog;
	protected String schema;
//	protected Map<String, TableInfo> tables = new HashMap<String, TableInfo>();
	protected List<String> tableNames = new ArrayList<String>();

	protected Connection connection;
	protected DatabaseManager databaseManager;
	protected GeneratorConfig config;
	protected TableInfoFactory tableFactory;
	protected ColumnInfoFactory columnFactory;
	protected PrimaryKeyFactory pkFactory;
	
	public DefaultTableManager() {
	}
	
	public DefaultTableManager(Connection connection, GeneratorConfig config) {
		this.connection = connection;
		this.config = config;
	}

	public DatabaseMetaData getMetaData() {
		return DBUtils.getMetaData(getConnection());
	}
	

	/*protected TableManager cacheTable(TableInfo table) {
		if (table != null) {
//			this.tables.put(table.getName(), table);
		}
		return this;
	}*/

	public TableInfo getTable(String tableName) {
		TableInfo table = null;
		table = this.createTable(tableName);
		return table;
	}

	/*public TableInfo getTables(List<String> tableNames, boolean refresh) {
		TableInfo table = null;
		if(!refresh)
			table = this.tables.get(tableName);
		if (table == null)
			table = this.createTable(tableName);
		return table;
	}*/
	


	public TableInfo createTable(String tableName) {
		return createTable(tableName, true);
	}

	public TableInfo createTable(String tableName, boolean autoClose) {
		TableInfo table = null;
		DBConnecton dbcon = null;
		ResultSet rs = null;
		try {
			Map<String, Object> rowMap = null;
			if(this.databaseManager.isOracle()){
				dbcon = new DBConnecton(this.getConnection());
				rs = dbcon.query(OracleSql.select_table_comments, "tableName", tableName);
				if(rs.next()){
					rowMap = DBUtils.toMap(rs);
					rowMap.put("REMARKS", rowMap.get("COMMENTS"));
				}
			}else{
				rs = getMetaData().getTables(catalog, schema, tableName.trim(), null);
				if(rs.next())
					rowMap = DBUtils.toMap(rs);
			}
			table = tableFactory.createTableInfo(rowMap);
			if (table!=null) {
				this.createColumns(table).createTablePrimaryKey(table);
//				this.cacheTable(table);
			}else
				throw new RuntimeException("can't create table info error: " + tableName);
		} catch (Exception e) {
			throw new RuntimeException("get table info error!", e);
		} finally{
			if(rs!=null)
				DBUtils.closeResultSet(rs);
			if(dbcon!=null)
				dbcon.closePreparedStatement();
			if(autoClose)
				closeConnection();
		}
		return table;
	}

	public DefaultTableManager createColumns(TableInfo table) throws SQLException {
		ResultSet rs = null;
		List<BaseMap> datas = null;
		rs = this.getMetaData().getColumns(catalog, schema, table.getName(), null);
		datas = DBUtils.toList(rs, true, new ResultSetMapper(){

			@Override
			public NonCaseMap map(ResultSet rs, NonCaseMap map) throws SQLException {
				map.put("COLUMN_NAME", rs.getString("COLUMN_NAME"));
				map.put("DATA_TYPE", rs.getInt("DATA_TYPE"));
				map.put("REMARKS", rs.getString("REMARKS"));
				return map;
			}
			
		});
		
		if(this.databaseManager.isOracle()){
			DBConnecton dbcon = new DBConnecton(this.getConnection());
			try {
				rs = dbcon.query(OracleSql.select_column_comments, "tableName", table.getName());
				Map comments = MyUtils.toMap(DBUtils.toList(rs, true), "column_name", "comments");

				for(Map row : datas){
					row.put("REMARKS", comments.get(row.get("COLUMN_NAME")));
				}
			} finally {
				dbcon.closePreparedStatement();
			}
			
		}
		
		columnFactory.createColumnInfo(datas, table);
		
		return this;
	}

	protected DefaultTableManager createTablePrimaryKey(TableInfo table) throws SQLException {
		ResultSet rs = null;
		try {
			rs = this.getMetaData().getPrimaryKeys(catalog, schema, table.getName());
			this.pkFactory.createPrimaryKey(rs, table);
		} catch (Exception e) {
			throw new ServiceException("createTablePrimaryKey error ", e);
		} finally{
			DBUtils.closeResultSet(rs);
		}
		return this;
	}
	


	public List<TableInfo> getTables(List<String> tnames) {
		return createTables(tnames.toArray(new String[tnames.size()]));
	}

	public List<TableInfo> getTables() {
		return createTables();
	}

	public List<TableInfo> createTables(String...tnames) {
		
		List<String> tableNames = null;
		if(tnames==null || tnames.length==0)
			tableNames = this.getTableNames(false);
		else
			tableNames = MyUtils.asList(tnames);
		
		List<TableInfo> tableInfos = new ArrayList<TableInfo>();
		try {
			for(String tableName : tableNames){
				TableInfo tinfo = createTable(tableName, false);
				tableInfos.add(tinfo);
			}
		} finally{
			closeConnection();
		}
		return tableInfos;
	}
	
	protected void closeConnection(){
		DBUtils.closeCon(this.connection);
		this.connection = null;
	}

	public List<String> getTableNames(boolean refrech) {
		if(!refrech){
			if(!this.tableNames.isEmpty())
				return this.tableNames;
		}
		this.tableNames.clear();
		ResultSet rs = null;
		try {
			if(databaseManager.isOracle()){
				DBConnecton dbcon = new DBConnecton(this.getConnection());
				rs = dbcon.query(OracleSql.select_all_table);
			}else
				rs = this.getMetaData().getTables(catalog, schema, null, null);
			while (rs.next()) {
				String tableName = rs.getString("TABLE_NAME");
				this.tableNames.add(tableName);
//				System.out.println("tablename"+this.tableNames.size()+": " + tableName);
			}
		} catch (Exception e) {
			throw new RuntimeException("get tables error!", e);
		} finally{
			DBUtils.closeResultSet(rs);
			closeConnection();
		}
		return this.tableNames;
	}

	public Connection getConnection() {
		try {
			if((connection==null || connection.isClosed()) && databaseManager!=null)
				connection = this.databaseManager.getConnection();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void setDatabaseManager(DatabaseManager dm) {
		this.databaseManager = dm;
	}

	public GeneratorConfig getConfig() {
		return config;
	}

	public void setGeneratorConfig(GeneratorConfig config) {
		this.config = config;
	}

	public TableInfoFactory getTableFactory() {
		return tableFactory;
	}

	public void setTableFactory(TableInfoFactory tableFactory) {
		this.tableFactory = tableFactory;
	}

	public void setColumnFactory(ColumnInfoFactory columnFactory) {
		this.columnFactory = columnFactory;
	}

	public void setPkFactory(PrimaryKeyFactory pkFactory) {
		this.pkFactory = pkFactory;
	}

	
}
