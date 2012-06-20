package org.onetwo.eclipse.custommenu.preference;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.onetwo.eclipse.AbstractPreferencePage;
import org.onetwo.eclipse.codegen.CodegenPlugin;

public class CustemMenuPreferencePage extends AbstractPreferencePage {
	
	public static class FieldNames {
		public static final String MENU_CUSTOM_FILE = "menu.custom.file";
		public static final String WEBSERVER_DEPLOY_DIR = "menu.custom.webserver.deploy.dir";
	}
	
	public static enum MenuType {
		cmd
	}

	protected FileFieldEditor customMenuFile;
	protected DirectoryFieldEditor webserverDir;
	
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(CodegenPlugin.getDefault().getPreferenceStore());
		setTitle("配置右键菜单的定制文件");
		setTitle("定制右键菜单");
	}

	@Override
	protected void createFieldEditors() {
		this.customMenuFile = addFileFieldEditor(FieldNames.MENU_CUSTOM_FILE, "菜单配置文件");
		this.webserverDir = addDirectoryFieldEditor(FieldNames.WEBSERVER_DEPLOY_DIR, "服务器目录");
	}
}
