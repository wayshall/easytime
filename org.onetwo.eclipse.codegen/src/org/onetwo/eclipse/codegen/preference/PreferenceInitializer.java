package org.onetwo.eclipse.codegen.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.onetwo.eclipse.PluginUtils;
import org.onetwo.eclipse.codegen.CodegenPlugin;
import org.onetwo.eclipse.codegen.CodegenUtils.DatabaseType;
import org.onetwo.eclipse.codegen.Constant.PrefercenceKeys;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = CodegenPlugin.getDefault().getPreferenceStore();
//		store.setDefault(PrefercenceKeys.project_dir, PluginUtils.getWorkSpacePath());
		store.setDefault(PrefercenceKeys.out_dir, PluginUtils.getWorkSpacePath());
		store.setDefault(PrefercenceKeys.template_dir, CodegenPlugin.getCodegenFacade().getDefaultTemplateDir());
		store.setDefault(PrefercenceKeys.db_type, DatabaseType.mysql);
	}

}
