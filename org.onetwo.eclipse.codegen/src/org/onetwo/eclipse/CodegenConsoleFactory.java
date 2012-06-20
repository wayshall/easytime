package org.onetwo.eclipse;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsoleFactory;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.onetwo.eclipse.codegen.CodegenPlugin;

public class CodegenConsoleFactory implements IConsoleFactory {
	
	public static String CODEGEN_CONSOLE = "codegen console";

	private IConsoleManager consoleMgr = ConsolePlugin.getDefault().getConsoleManager();

	@Override
	public void openConsole() {
		MessageConsole console = CodegenPlugin.getDefault().getMessageConsole();
		consoleMgr.showConsoleView(console);
		/*MessageConsoleStream stream = console.newMessageStream();
		PrintStream pw = new PrintStream(stream);
		System.setOut(pw);
		System.setErr(pw);*/
	}

}
