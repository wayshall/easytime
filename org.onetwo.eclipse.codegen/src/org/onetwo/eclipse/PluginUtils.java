package org.onetwo.eclipse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.onetwo.core.util.AExecute;
import org.onetwo.core.util.StringUtils;
import org.onetwo.eclipse.codegen.CodegenPlugin;

@SuppressWarnings("restriction")
abstract public class PluginUtils {

	public static File getDirectory(Object resource) {
		File directory = null;
		if ((resource instanceof IResource))
			directory = new File(((IResource) resource).getLocation().toOSString());
		else if ((resource instanceof File)) {
			directory = (File) resource;
		}
		if ((resource instanceof IFile)) {
			directory = directory.getParentFile();
		}
		if ((resource instanceof File)) {
			directory = directory.getParentFile();
		}
		if(resource instanceof IFileEditorInput){
	    	IFileEditorInput input = (IFileEditorInput) resource;
	    	directory = input.getFile().getRawLocation().toFile().getParentFile();
	    }
		return directory;
	}
	
	public static File getProjectDirectory(Object resource) {
		File directory = null;
		if ((resource instanceof IResource))
			directory = new File(((IResource) resource).getProjectRelativePath().toOSString());
		else if ((resource instanceof File)) {
			directory = (File) resource;
		}
		if ((resource instanceof IFile)) {
			directory = directory.getParentFile();
		}
		if ((resource instanceof File)) {
			directory = directory.getParentFile();
		}
		if(resource instanceof IFileEditorInput){
	    	IFileEditorInput input = (IFileEditorInput) resource;
	    	directory = input.getFile().getRawLocation().toFile().getParentFile();
	    }
		return directory;
	}

	public static Object getResource(IAdaptable adaptable) {
		Object selected = "unknown";
		if ((adaptable instanceof IResource))
			selected = ((IResource) adaptable);
		else if (((adaptable instanceof PackageFragment)) && ((((PackageFragment) adaptable).getPackageFragmentRoot() instanceof JarPackageFragmentRoot)))
			selected = getJarFile(((PackageFragment) adaptable).getPackageFragmentRoot());
		else if ((adaptable instanceof JarPackageFragmentRoot))
			selected = getJarFile(adaptable);
		else
			selected = ((IResource) adaptable.getAdapter(IResource.class));
		return selected;
	}

	public static File getJarFile(IAdaptable adaptable) {
		JarPackageFragmentRoot jpfr = (JarPackageFragmentRoot) adaptable;
		File selected = jpfr.getPath().makeAbsolute().toFile();
		if (!selected.exists()) {
			File projectFile = new File(jpfr.getJavaProject().getProject().getLocation().toOSString());
			selected = new File(projectFile.getParent() + selected.toString());
		}
		return selected;
	}

	public static String getPluginPath() {
		String path = getPluginURL().getPath();
		if (path.startsWith("/"))
			path = path.substring(1);
		if (path.endsWith("/"))
			path = path.substring(0, path.length() - 1);
		path.replace("//", "/");
		return path;
	}

	public static URL getPluginURL() {
		URL url = FileLocator.find(CodegenPlugin.getDefault().getBundle(), new Path("/"), null);
		try {
			return FileLocator.toFileURL(url);
		} catch (IOException e) {
			throw new RuntimeException("can find the path!");
		}
	}

	public static InputStream getInputStream(String path) {
		return PluginUtils.class.getResourceAsStream(path);
	}

	public static URL getWorkSpaceURL() {
		URL url = Platform.getInstanceLocation().getURL();
		return url;
	}

	public static String getWorkSpacePath() {
		String path = getWorkSpaceURL().getPath();
		if (path != null) {
			path = path.startsWith("/") ? path.substring(1) : path;
			path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
			path = path.replace('/', java.io.File.separatorChar);
		}
		return path;
	}


	public static Object getCurrentSelected(IWorkbench workbench){
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		return getCurrentSelected(window);
	}

	public static Object getCurrentSelected(ExecutionEvent event){
		IWorkbenchWindow window;
		try {
			window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
			return getCurrentSelected(window);
		} catch (ExecutionException e) {
			CodegenPlugin.log(e);
		}
		return null;
	}
	
	public static Object getCurrentSelected(IWorkbenchWindow window){
		  
	    ISelection selection = window.getSelectionService().getSelection();
		IAdaptable adaptable = null;
		
		Object selected = "unknown";
		Class<?> selectedClass = null;
		if ((selection instanceof IStructuredSelection)) {
			adaptable = (IAdaptable) ((IStructuredSelection) selection).getFirstElement();
			selectedClass = adaptable.getClass();
			selected = PluginUtils.getResource(adaptable);
		}else if(selection instanceof ITextSelection){
		    IEditorInput editor = window.getActivePage().getActiveEditor().getEditorInput();
//		    System.out.println("editor: " + editor);
		    return editor;
		}
		if ("unknown".equals(selected)) {
//			MessageDialog.openInformation(new Shell(), "Easy Explore", "Unable to explore " + selectedClass.getName());
			CodegenPlugin.getDefault().consoleLog("Unable to explore " + selectedClass);
			return null;
		}
		
		return selected;
	}
	
	public static void run(boolean fork, boolean cancelable, IRunnableWithProgress runnable){
		IWorkbenchWindow window = CodegenPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
		IRunnableContext context = window.getWorkbench().getProgressService();
		try {
			context.run(fork, cancelable, runnable);
		} catch (Exception e) {
			CodegenPlugin.log(e);
		}
	}
	
	public static void runInProgress(final String msg, final AExecute execute){
		run(true, false, new IRunnableWithProgress() {
			
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				String msg1 = msg;
				if(StringUtils.isBlank(msg))
					msg1 = "please waiting...";
				monitor.beginTask(msg1, 1);
				execute.execute();
				monitor.worked(1);
				monitor.done();
			}
		});
	}
	
	public static void showMessage(String title, String message){
		MessageDialog.openInformation(CodegenPlugin.getDefault().getWorkbenchWindow().getShell(), title, message);
	}
}
