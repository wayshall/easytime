package org.onetwo.eclipse;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPreferencePage;

abstract public class AbstractPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	protected DirectoryFieldEditor addDirectoryFieldEditor(String name, String label){
		Composite parentComposite = getFieldEditorParent();
		DirectoryFieldEditor projectDir = new DirectoryFieldEditor(name, label, parentComposite);
		addField(projectDir);
		return projectDir;
	}
	
	protected FileFieldEditor addFileFieldEditor(String name, String label){
		Composite parentComposite = getFieldEditorParent();
		FileFieldEditor file = new FileFieldEditor(name, label, parentComposite);
		addField(file);
		return file;
	}
	
	protected StringFieldEditor addStringField(String name, String label){
		Composite parentComposite = getFieldEditorParent();
		StringFieldEditor field = new StringFieldEditor(name, label, parentComposite);
		addField(field);
		return field;
	}
	protected IntegerFieldEditor addIntField(String name, String label){
		Composite parentComposite = getFieldEditorParent();
		IntegerFieldEditor field = new IntegerFieldEditor(name, label, parentComposite);
		addField(field);
		return field;
	}
	
}
