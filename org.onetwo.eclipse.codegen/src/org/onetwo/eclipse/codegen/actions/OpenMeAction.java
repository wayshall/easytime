package org.onetwo.eclipse.codegen.actions;

import java.io.File;
import java.text.MessageFormat;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.onetwo.eclipse.PluginUtils;
import org.onetwo.eclipse.codegen.CodegenPlugin;

/***********
 * 移植（或者叫复制吧）easy explore
 *
 */

@SuppressWarnings("restriction")
public class OpenMeAction implements IObjectActionDelegate {

	private Shell shell;
	private Object selected = null;
	private Class selectedClass = null;

	/**
	 * Constructor for Action1.
	 */
	public OpenMeAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	public void run(IAction action) {
		try {
			if ("unknown".equals(this.selected)) {
				MessageDialog.openInformation(new Shell(), "Easy Explore", "Unable to explore " + this.selectedClass.getName());
				CodegenPlugin.log("Unable to explore " + this.selectedClass);
				return;
			}
			File directory = PluginUtils.getDirectory(this.selected);
			
			String target = CodegenPlugin.getDefault().getOpenMeCommand();

			if (!CodegenPlugin.getDefault().isSupported()) {
				MessageDialog.openInformation(new Shell(), "Easy Explore", "This platform (" + System.getProperty("os.name") + ") is currently unsupported.\n" + "You can try to provide the correct command to execute in the Preference dialog.\n" + "If you succeed, please be kind to post your discovery on EasyExplore website http://sourceforge.net/projects/easystruts,\n" + "or by email farialima@users.sourceforge.net. Thanks !");
				return;
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
	}

	public void selectionChanged(IAction action, ISelection selection) {
		try {
			IAdaptable adaptable = null;
			this.selected = "unknown";
			if ((selection instanceof IStructuredSelection)) {
				adaptable = (IAdaptable) ((IStructuredSelection) selection).getFirstElement();
				this.selectedClass = adaptable.getClass();
				this.selected = PluginUtils.getResource(adaptable);
			}
		} catch (Throwable e) {
			CodegenPlugin.log(e);
		}
	}

	
}