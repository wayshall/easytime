package org.onetwo.eclipse.codegen;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.core.codegenerator.GeneratorConfig;
import org.onetwo.eclipse.codegen.CodegenUtils.JdbcKeys;

abstract public class Constant {
	
	public static final String preferencePageId = "org.onetwo.eclipse.codegen.page";

	public static class PrefercenceKeys {
//		public static final String project_dir = "project.dir";
		public static final String out_dir = GeneratorConfig.GENERATE_OUT_DIR;
		public static final String template_dir = GeneratorConfig.TEMPLATE_DIR;
		

		
		public static final String jdbc_url = JdbcKeys.url;
		public static final String jdbc_user = JdbcKeys.username;
		public static final String jdbc_password = JdbcKeys.password;

		public static final String db_type = JdbcKeys.db_type;
		public static final String db_host = JdbcKeys.db_host;
		public static final String db_port = JdbcKeys.db_port;
		public static final String db_name = JdbcKeys.db_name;

		@SuppressWarnings("serial")
		private static List<String> values = new ArrayList<String>(){
			{
//				add(project_dir);
				add(out_dir);
				add(template_dir);
				
				add(jdbc_url);
				add(jdbc_user);
				add(jdbc_password);
				
				add(db_type);
				add(db_host);
				add(db_port);
				add(db_name);
			}
		};
		
		public static List<String> getValues(){
			return values;
		}
	}

	public static class RTKeys {
		public static final String base_pageckage = GeneratorConfig.JAVA_PAGECKAGE;//"base.pageckage";
		public static final String generate_tables = "generate.tables";
		public static final String table_prefix = "table.prefix";

		@SuppressWarnings("serial")
		private static List<String> values = new ArrayList<String>(){
			{
				add(base_pageckage);
				add(generate_tables);
				add(table_prefix);
			}
		};
		
		public static List<String> getValues(){
			return values;
		}
	}
	
}
