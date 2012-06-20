package org.onetwo.eclipse.codegen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.jface.preference.IPreferenceStore;
import org.onetwo.core.codegenerator.db.DatabaseManager;
import org.onetwo.core.util.StringUtils;
import org.onetwo.eclipse.codegen.Constant.PrefercenceKeys;


@SuppressWarnings("serial")
public class CodegenUtils {
	
	public static class JdbcKeys {
		public static final String driver_class = "jdbc.driverClass";
		public static final String url = "jdbc.url";
		public static final String username = "jdbc.username";
		public static final String password = "jdbc.password";
		
		public static final String db_type = DatabaseManager.DBKeys.type;		
		public static final String db_host = DatabaseManager.DBKeys.host;
		public static final String db_port = DatabaseManager.DBKeys.port;
		public static final String db_name = DatabaseManager.DBKeys.dbname;

		
		public static List<String> values = new ArrayList<String>(){
			{
				add(driver_class);
				add(url);
				add(username);
				add(password);

				add(db_type);
				add(db_host);
				add(db_port);
				add(db_name);
			}
		};
		
	}
	
	public static class DatabaseType {
		public static final String oracle = "oracle";
		public static final String mysql = "mysql";

	}
	private static Map<String, Properties> configMap;
	
	static {
		configMap = new HashMap<String, Properties>(){
			{
				put(DatabaseType.mysql, new Properties(){
					{
						setProperty(JdbcKeys.driver_class, "com.mysql.jdbc.Driver");
						setProperty(JdbcKeys.url, "");
						setProperty(JdbcKeys.username, "root");
						setProperty(JdbcKeys.password, "");
					}
				});
				
				put(DatabaseType.oracle, new Properties(){
					{
						setProperty(JdbcKeys.driver_class, "oracle.jdbc.driver.OracleDriver");
						setProperty(JdbcKeys.url, "");
						setProperty(JdbcKeys.username, "root");
						setProperty(JdbcKeys.password, "");
					}
				});
			}
		};
	}

	private CodegenUtils(){}

	public static Map<String, Properties> getConfigMap() {
		return configMap;
	};
	
	public static String[][] getDatabaseTypes(){
		String[][] configs = new String[configMap.size()][];
		int index = 0;
		for(String key : configMap.keySet()){
			configs[index] = new String[]{key, key};
			index++;
		}
		return configs;
	}
	
	public static Properties getDatabaseConfig(IPreferenceStore store){
		String dbtype = store.getString(PrefercenceKeys.db_type);
		Properties props;
		if(StringUtils.isNotBlank(dbtype)){
			props = configMap.get(dbtype);
		}else{
			props = new Properties();
		}
		for(String key : JdbcKeys.values){
			String val = store.getString(key);
			if(StringUtils.isBlank(val))
				continue;
			props.setProperty(key, val);
		}
		return props;
	}
}
