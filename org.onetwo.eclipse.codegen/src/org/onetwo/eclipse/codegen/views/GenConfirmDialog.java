package org.onetwo.eclipse.codegen.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.onetwo.core.codegenerator.GeneratorConfig;
import org.onetwo.core.util.StringUtils;
import org.onetwo.eclipse.codegen.CodegenPlugin;
import org.onetwo.eclipse.codegen.Constant.RTKeys;
import org.onetwo.eclipse.codegen.views.TableView.TreeObject;

@SuppressWarnings("unchecked")
public class GenConfirmDialog extends ApplicationWindow {

	private Composite composite;
	
	private Text txtBasepack;
	private Text tablePrerix;
	private Group checkboxGroup;
	
	private List<TreeObject> tableObjects;
	
	public GenConfirmDialog(Shell parentShell, List<TreeObject> tableObjects) {
		super(parentShell);
		/*IShellProvider provider = new SameShellProvider(parentShell);
		setDefaultModalParent(provider);*/
		setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | getDefaultOrientation());
		setBlockOnOpen(true);
		
		this.tableObjects = tableObjects;
	}

	@Override
	protected Control createContents(Composite parent) {
		CodegenPlugin.getCodegenFacade().clearBuilders();
		final Shell shell = getShell();
		shell.setSize(400, 250);
		composite = new Composite(parent, SWT.NONE);
		
		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);
		
		createLabel(composite, Messages.getString("GenConfirmDialog.0")); //$NON-NLS-1$
		txtBasepack = createText(composite);
		
		createLabel(composite, Messages.getString("GenConfirmDialog.1")); //$NON-NLS-1$
		tablePrerix = createText(composite);

		/*Label labelGenType = createLabel(composite, "选择要生成的代码类型：");
		GridData gdGenType = this.createGridData();
		gdGenType.horizontalAlignment = SWT.LEFT;
		gdGenType.horizontalSpan = 2;
		labelGenType.setLayoutData(gdGenType);*/
		
		checkboxGroup = new Group(composite, SWT.SHADOW_IN);
		GridLayout checkboxLayout = new GridLayout(2, false);
		checkboxGroup.setLayout(checkboxLayout);
		GridData gdGenType = this.createGridData();
		gdGenType.horizontalAlignment = SWT.CENTER;
		gdGenType.horizontalSpan = 2;
		checkboxGroup.setLayoutData(gdGenType);
		checkboxGroup.setText(Messages.getString("GenConfirmDialog.2")); //$NON-NLS-1$

		List<String> templates = CodegenPlugin.getCodegenFacade().getTemplates();
		if(templates!=null && !templates.isEmpty()){
			for(String t : templates){
				Button btn = createButton(checkboxGroup, t, SWT.CHECK);
				btn.addSelectionListener(new SelectionAdapter(){

					@Override
					public void widgetSelected(SelectionEvent e) {
						Button s = (Button)e.getSource();
						if(s.getSelection())
							CodegenPlugin.getCodegenFacade().addBuilder(s.getText());
						else
							CodegenPlugin.getCodegenFacade().removeBuilder(s.getText());
					}
					
				});
			}
		}else{
			showMessage(Messages.getString("GenConfirmDialog.3")); //$NON-NLS-1$
			close();
			return null;
		}
		
		String tableName1 = this.getTableObjects().get(0).getName();
		int prefixIndex = tableName1.indexOf('_');
		if(prefixIndex!=-1){
			tableName1 = tableName1.substring(0, prefixIndex+1);
			tablePrerix.setText(tableName1);
		}

		GridData gdOk = this.createGridData();
		gdOk.horizontalAlignment = SWT.CENTER;
		gdOk.horizontalSpan = 2;
		
		Composite btns = new Composite(composite, SWT.NULL);
		btns.setLayout(new RowLayout(SWT.HORIZONTAL));
		btns.setLayoutData(gdOk);
		
		Button btnOk = createButton(btns, Messages.getString("GenConfirmDialog.4"), SWT.PUSH); //$NON-NLS-1$
		btnOk.setFocus();
		
		btnOk.addMouseListener(new MouseAdapter(){

			@Override
			public void mouseUp(MouseEvent e) {
				if(!verifyInput()){
					return ;
				}
				
				try {
					String basePackage = txtBasepack.getText();
					GeneratorConfig config = CodegenPlugin.getCodegenFacade().getConfig();
					config.setProperty(RTKeys.base_pageckage, basePackage);
					config.setProperty(RTKeys.table_prefix, tablePrerix.getText());
					CodegenPlugin.getCodegenFacade().generateTable(getTableNames());
					showMessage(Messages.getString("GenConfirmDialog.5")); //$NON-NLS-1$
//					close();
				} catch (Exception ee) {
					showMessage(Messages.getString("GenConfirmDialog.6")+ee.getMessage()); //$NON-NLS-1$
				}
			}
			
		});
		
		Button btnCancel = createButton(btns, Messages.getString("GenConfirmDialog.7"), SWT.PUSH); //$NON-NLS-1$

		btnCancel.addMouseListener(new MouseAdapter(){

			@Override
			public void mouseUp(MouseEvent e) {
				close();
			}
			
		});
		
		return composite;
	}
	
	protected boolean verifyInput(){
		String basePackage = txtBasepack.getText();
		if(StringUtils.isBlank(basePackage)){
			showMessage(Messages.getString("GenConfirmDialog.8")); //$NON-NLS-1$
			return false;
		}
		try {
			Control[] btnChildren = (Control[]) this.checkboxGroup.getChildren();
			boolean check = false;
			for(Control btn : btnChildren){
				if(((Button)btn).getSelection()){
					check = true;
					break;
				}
			}
			if(!check){
				showMessage(Messages.getString("GenConfirmDialog.9")); //$NON-NLS-1$
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		
		return true;
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(
			getShell(),
			Messages.getString("GenConfirmDialog.10"), //$NON-NLS-1$
			message);
	}

	public Label createLabel(Composite composite, String text) {
		Label label = new Label(composite, SWT.WRAP);
		label.setText(text);
		return label;
	}

	public Text createText(Composite composite) {
		Text text = new Text(composite, SWT.BORDER);
		GridData gdtext = this.createGridData();
		gdtext.horizontalAlignment = GridData.FILL;
		gdtext.grabExcessHorizontalSpace = true;
		text.setLayoutData(gdtext);
		return text;
	}
	
	protected Button createButton(Composite composite, String label, int type){
		Button btn = new Button(composite, type);
		btn.setText(label);
		return btn;
	}
	
	protected GridData createGridData(){
		GridData data = new GridData();
		return data;
	}

	public List<TreeObject> getTableObjects() {
		return tableObjects;
	}

	public void setTableObjects(List<TreeObject> tableObjects) {
		this.tableObjects = tableObjects;
	}
	
	public List<String> getTableNames(){
		if(this.tableObjects==null)
			return Collections.EMPTY_LIST;
		List<String> tnames = new ArrayList<String>();
		for(TreeObject t : this.tableObjects){
			tnames.add(t.getName());
		}
		return tnames;
	}

}
