package org.onetwo.eclipse.codegen.handlers;

import java.io.File;
import java.text.MessageFormat;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.onetwo.eclipse.PluginUtils;
import org.onetwo.eclipse.codegen.CodegenPlugin;

public class OpenMeHandler extends AbstractHandler{

//	private Object selected = null;
//	private Class selectedClass = null;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
//			Object selected = PluginUtils.getCurrentSelected(event);
			Object selected = PluginUtils.getCurrentSelected(CodegenPlugin.getDefault().getWorkbench());
			if(selected==null)
				return null;
			
			File directory = PluginUtils.getDirectory(selected);
			String target = CodegenPlugin.getDefault().getOpenMeCommand();

			if (!CodegenPlugin.getDefault().isSupported()) {
				MessageDialog.openInformation(new Shell(), "Easy Explore", "This platform (" + System.getProperty("os.name") + ") is currently unsupported.\n" + "You can try to provide the correct command to execute in the Preference dialog.\n" + "If you succeed, please be kind to post your discovery on EasyExplore website http://sourceforge.net/projects/easystruts,\n" + "or by email farialima@users.sourceforge.net. Thanks !");
				return null;
			}

			if (target.indexOf("{0}") == -1) {
				target = target.trim() + " {0}";
			}

			target = MessageFormat.format(target, new String[] { directory.toString() });
			try {
				CodegenPlugin.log("running: " + target);
				Runtime.getRuntime().exec(target);
			} catch (Throwable t) {
				MessageDialog.openInformation(new Shell(), "Easy Explore", "Unable to execute " + target);
				CodegenPlugin.log(t);
			}
			
		} catch (Throwable e) {
			CodegenPlugin.log(e);
		}
		return null;
	}

}
