package org.onetwo.eclipse.codegen;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.onetwo.core.util.StringUtils;
import org.onetwo.eclipse.CodegenConsoleFactory;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class CodegenPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.onetwo.eclipse.codegen";
	// The plug-in ID
	public static final String OPEN_ME = "org.onetwo.eclipse.codegen.preference.open.me";

	@SuppressWarnings("serial")
	private final List<String> supportedOS = new ArrayList<String>() {
		{
			add("Windows");
			add("Mac");
		}
	};

	// The shared instance
	private static CodegenPlugin plugin;

	private static CodegenFacade codegenFacade;

	/**
	 * The constructor
	 */
	public CodegenPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		codegenFacade = new DefaultCodegenFacade();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		codegenFacade.destroy();
		codegenFacade = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static CodegenPlugin getDefault() {
		return plugin;
	}

	public static CodegenFacade getCodegenFacade() {
		return codegenFacade;
	}
	
	public String getPreferenceString(String key, String def){
		String val = getPreferenceStore().getString(key);
		if(StringUtils.isBlank(val))
			return def;
		return val;
	}

	protected String initDefaultPreferences(IPreferenceStore store) {
		String defaultTarget = "shell_open_command {0}";
		String osName = System.getProperty("os.name");
		if (osName.indexOf("Windows") != -1) {
			defaultTarget = "explorer.exe {0}";
		} else if (osName.indexOf("Mac") != -1) {
			defaultTarget = "open {0}";
		}

		store.setDefault(OPEN_ME, defaultTarget);
		
		return defaultTarget;
	}
	
	public String getOpenMeCommand(){
		String cmd = getPreferenceStore().getString(OPEN_ME);
		if(StringUtils.isBlank(cmd)){
			cmd = initDefaultPreferences(getPreferenceStore());
		}
		return cmd;
	}
	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public boolean isSupported() {
		String osName = System.getProperty("os.name");
		for (String os : supportedOS) {
			if (osName.indexOf(os) != -1)
				return true;
		}
		return false;
	}
	
	public IWorkbenchWindow getWorkbenchWindow(){
		return getWorkbench().getActiveWorkbenchWindow();
	}
	
	public MessageConsole getMessageConsole(){
		IConsoleManager consoleMgr = ConsolePlugin.getDefault().getConsoleManager();
		IConsole[] consoles = consoleMgr.getConsoles();
		if(consoles!=null){
			for(IConsole c : consoles){
				if(c.getName().equals(CodegenConsoleFactory.CODEGEN_CONSOLE)){
					return (MessageConsole)c;
				}
			}
		}
		MessageConsole console = new MessageConsole(CodegenConsoleFactory.CODEGEN_CONSOLE, null);
		consoleMgr.addConsoles(new IConsole[]{console});
		return console;
	}
	
	public MessageConsoleStream getMessageConsoleStream(){
		MessageConsole console = getMessageConsole();
		MessageConsoleStream stream = console.newMessageStream();
		return stream;
	}
	
	public void consoleLog(String msg){
		MessageConsoleStream stream = getMessageConsoleStream();
		consoleLog(stream, msg);
	}
	
	public void consoleLog(MessageConsoleStream stream, String msg){
		stream.setActivateOnWrite(true);
		if(msg==null)
			stream.println();
		else
			stream.println(msg);
	}

	public static void log(Object msg) {
		ILog log = getDefault().getLog();
		Status status = new Status(1, getDefault().getBundle().getBundleId() + "", 4, msg + "\n", null);
		log.log(status);
	}

	public static void log(Throwable e) {
		ILog log = getDefault().getLog();
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		String msg = stringWriter.getBuffer().toString();
		Status status = new Status(4, getDefault().getBundle().getBundleId() + "", 4, msg, null);
		log.log(status);
	}
}
