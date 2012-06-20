package org.onetwo.eclipse.codegen.preference;

import java.util.Properties;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.onetwo.core.codegenerator.GeneratorConfig;
import org.onetwo.core.util.StringUtils;
import org.onetwo.eclipse.AbstractPreferencePage;
import org.onetwo.eclipse.codegen.CodegenPlugin;
import org.onetwo.eclipse.codegen.CodegenUtils;
import org.onetwo.eclipse.codegen.Constant.PrefercenceKeys;

public class CodegenPreferencePage extends AbstractPreferencePage {

	
	private DirectoryFieldEditor projectDir;
	private DirectoryFieldEditor outDir;
	private DirectoryFieldEditor templateDir;
	
	private StringFieldEditor dburl;
	private StringFieldEditor dbuser;
	private StringFieldEditor dbpwd;
	
	private StringFieldEditor dbhost;
	private IntegerFieldEditor dbport;
	private StringFieldEditor dbname;
	
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(CodegenPlugin.getDefault().getPreferenceStore());
		setTitle(Messages.getString("CodegenPreferencePage.0")); //$NON-NLS-1$
		setDescription(Messages.getString("CodegenPreferencePage.1")); //$NON-NLS-1$
	}

	@Override
	protected void createFieldEditors() {
		final Shell shell = getShell();
		Composite parentComposite = getFieldEditorParent();
		RowLayout rlayout = new RowLayout();
		rlayout.spacing = 10;
		parentComposite.setLayout(rlayout);
		
//		projectDir = addDirectoryFieldEditor(PrefercenceKeys.project_dir, "项目位置：");
		outDir = addDirectoryFieldEditor(PrefercenceKeys.out_dir, Messages.getString("CodegenPreferencePage.2")); //$NON-NLS-1$
		templateDir = addDirectoryFieldEditor(PrefercenceKeys.template_dir, Messages.getString("CodegenPreferencePage.3")); //$NON-NLS-1$
		
		RadioGroupFieldEditor dbTypeGroup = new RadioGroupFieldEditor(PrefercenceKeys.db_type, Messages.getString("CodegenPreferencePage.4"), 1, CodegenUtils.getDatabaseTypes(), getFieldEditorParent(), true); //$NON-NLS-1$
		addField(dbTypeGroup);

		dburl = addStringField(PrefercenceKeys.jdbc_url, Messages.getString("CodegenPreferencePage.5")); //$NON-NLS-1$

		dbhost = addStringField(PrefercenceKeys.db_host, Messages.getString("CodegenPreferencePage.6")); //$NON-NLS-1$
		dbport = addIntField(PrefercenceKeys.db_port, Messages.getString("CodegenPreferencePage.7")); //$NON-NLS-1$
		dbname = addStringField(PrefercenceKeys.db_name, Messages.getString("CodegenPreferencePage.8")); //$NON-NLS-1$
		
		dbuser = addStringField(PrefercenceKeys.jdbc_user, Messages.getString("CodegenPreferencePage.9")); //$NON-NLS-1$
		dbpwd = addStringField(PrefercenceKeys.jdbc_password, Messages.getString("CodegenPreferencePage.10")); //$NON-NLS-1$
		
		/*addField(dburl);

		addField(dbhost);
		addField(dbport);
		addField(dbname);
		
		addField(dbuser);
		addField(dbpwd);*/
		
		Button btnTestdb = new Button(parentComposite, SWT.FLAT);
		btnTestdb.setText(Messages.getString("CodegenPreferencePage.11")); //$NON-NLS-1$
		btnTestdb.addMouseListener(new MouseAdapter(){

			@Override
			public void mouseUp(MouseEvent e) {
				CodegenPreferencePage.this.performOk();
				Properties dbconfig = CodegenUtils.getDatabaseConfig(CodegenPreferencePage.this.getPreferenceStore());
				
				boolean rs = false;
				String msg = "";
				try{
//					rs = CodegenPlugin.getCodegenFacade().isDbconnectSuccess(dbconfig);
					String databasename = CodegenPlugin.getCodegenFacade().getDatabaseName();
					rs = StringUtils.isNotBlank(databasename);
					msg = Messages.getString("CodegenPreferencePage.12"); //$NON-NLS-1$
					if(rs)
						msg = Messages.getString("CodegenPreferencePage.13")+databasename+Messages.getString("CodegenPreferencePage.14"); //$NON-NLS-1$ //$NON-NLS-2$
					else
						msg = Messages.getString("CodegenPreferencePage.15"); //$NON-NLS-1$
					System.out.println(Messages.getString("CodegenPreferencePage.16")+msg); //$NON-NLS-1$
				}catch(Exception ee){
					msg = Messages.getString("CodegenPreferencePage.15"); //$NON-NLS-1$
				}
				MessageDialog.openInformation(shell, Messages.getString("CodegenPreferencePage.17"), msg); //$NON-NLS-1$
			}
			
		});
		
	}

    protected void performApply() {
        performOk();
		IPreferenceStore store = getPreferenceStore();
		try {
			GeneratorConfig config = CodegenPlugin.getCodegenFacade().getConfig();
			for(String key : PrefercenceKeys.getValues()){
				String val = store.getString(key);
				if(PrefercenceKeys.template_dir.equals(key)){
					if(StringUtils.isBlank(val))
						val = CodegenPlugin.getCodegenFacade().getDefaultTemplateDir();
				}
				if(val==null)
					continue;
				config.setProperty(key, val);
			}
			config.saveToFile();
			MessageDialog.openInformation(getShell(), Messages.getString("CodegenPreferencePage.18"), Messages.getString("CodegenPreferencePage.19")); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (Exception e) {
			MessageDialog.openError(getShell(), Messages.getString("CodegenPreferencePage.20"), Messages.getString("CodegenPreferencePage.21") + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
		}
    }
	

	public DirectoryFieldEditor getProjectDir() {
		return projectDir;
	}

	public DirectoryFieldEditor getOutDir() {
		return outDir;
	}

	public DirectoryFieldEditor getTemplateDir() {
		return templateDir;
	}

}
