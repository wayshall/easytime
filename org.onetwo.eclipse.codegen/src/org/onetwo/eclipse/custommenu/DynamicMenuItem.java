package org.onetwo.eclipse.custommenu;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.console.MessageConsoleStream;
import org.onetwo.core.util.AExecute;
import org.onetwo.core.util.ArrayUtils;
import org.onetwo.core.util.CollectionUtils;
import org.onetwo.core.util.DateUtil;
import org.onetwo.core.util.Expression;
import org.onetwo.core.util.MyUtils;
import org.onetwo.core.util.StringUtils;
import org.onetwo.core.util.SysUtils;
import org.onetwo.eclipse.Constant.AntDir;
import org.onetwo.eclipse.PluginUtils;
import org.onetwo.eclipse.codegen.CodegenPlugin;
import org.onetwo.eclipse.custommenu.preference.CustemMenuPreferencePage.FieldNames;
import org.onetwo.eclipse.custommenu.preference.CustemMenuPreferencePage.MenuType;
import org.onetwo.ext.xml.DomUtils;
import org.onetwo.ext.xml.SimpleXmlObjectParser;
import org.onetwo.ext.xml.XmlObjectParser;

@SuppressWarnings("rawtypes")
public class DynamicMenuItem extends ContributionItem {

	private List<MenuConfig> popMenus;
	private File directory;
	private Expression expr = Expression.DOLOR_INSTANCE;
	
	private boolean debug;
	
	protected List<MenuConfig> readMenuConfig(){
		IPreferenceStore store = CodegenPlugin.getDefault().getPreferenceStore();
		String configFile = store.getString(FieldNames.MENU_CUSTOM_FILE);
		if(StringUtils.isBlank(configFile))
			return null;
		
		Document doc = DomUtils.readXml(new File(configFile));
		XmlObjectParser p = new SimpleXmlObjectParser();
		Map map = new HashMap();
		map.put("popmenu", null);//根元素有那些属性
		map.put("debug", null);//根元素有那些属性
		p.setRoot(map);//设置根元素
		p.map("popmenus", HashMap.class, true);//根元素对应类型
		p.map("popmenu", MenuConfig.class, false);//映射各元素对应的类型
		doc.accept(p);
		Map root = (Map)p.getRoot();
		this.debug = "true".equals(root.get("debug"));
		List<MenuConfig> popMenus = (List<MenuConfig>)MyUtils.asList(root.get("popmenu"));
		
		return popMenus;
	}

	@Override
	public void fill(Menu menu, int index) {
		this.popMenus = this.readMenuConfig();
		if(CollectionUtils.isEmpty(popMenus))
			return ;

		Object selected = PluginUtils.getCurrentSelected(CodegenPlugin.getDefault().getWorkbench());
		if(selected==null)
			return ;

		String type = selected.getClass().getSimpleName();
		if(debug)
			CodegenPlugin.getDefault().consoleLog("selected:"+type);
		for(final MenuConfig menuConfig : this.popMenus){
			if(menuConfig==null)
				continue;
			if(debug)
				CodegenPlugin.getDefault().consoleLog("menu["+menuConfig.getName()+"] dependency:"+menuConfig.getDependency());
			if(!menuConfig.isMatchDependency(type))
				continue;
			this.createMenuItem(selected, menu, index, menuConfig);
		}
	}
	
	protected void createMenuItem(final Object selected, Menu menu, int index, final MenuConfig menuConfig){
		MenuItem mitem = new MenuItem(menu, SWT.CHECK, index);
		mitem.setText(menuConfig.getName());
		
		if(MenuType.cmd.equals(MenuType.valueOf(menuConfig.getType()))){
			mitem.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent event) {
					final String[] cmds = menuConfig.getStatments();
					if(cmds==null)
						return ;
					IWorkbenchWindow window = CodegenPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
					IRunnableContext context = window.getWorkbench().getProgressService();
					try {
						context.run(true, true, new IRunnableWithProgress() {
							
							@Override
							public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
								monitor.beginTask("正在执行["+menuConfig.getName()+"]，请稍候……", cmds.length);

								final MessageConsoleStream stream = CodegenPlugin.getDefault().getMessageConsoleStream();
								
								File curFile = PluginUtils.getCurrentFile(selected);
//								System.out.println("curent:"+curFile);
								directory = PluginUtils.getDirectory(selected);
								String webserverDir = CodegenPlugin.getDefault().getPreferenceString(FieldNames.WEBSERVER_DEPLOY_DIR, null);

								Map<String, String> context = new HashMap<String, String>();
								context.put("cur_dir", directory.toString());
								context.put("dir", directory.toString());
								context.put("selected", selected.toString());
								context.put("deploy_dir", webserverDir);
								context.put("build_ejb", AntDir.ejb);
								context.put("build_web", AntDir.web);
								if(curFile!=null){
									context.put("cur_file", curFile.toString());
								}
								if(selected instanceof IProject){
									IProject iprj = (IProject)selected;
									context.put("project_name", iprj.getName());
								}
//								CodegenPlugin.getDefault().consoleLog(stream, "context:"+context);
//								File projectDir = PluginUtils.getProjectDirectory(selected);
//								CodegenPlugin.getDefault().consoleLog(stream, "dir:"+directory.toString());
//								String projectDirStr = Platform.getInstanceLocation().getURL().getPath();
//								CodegenPlugin.getDefault().consoleLog(stream, "project2:"+projectDirStr.toString());
								
								
								for(String cmd : cmds){
									if(StringUtils.isBlank(cmd))
										continue;
									cmd = cmd.trim();
									executeMenuCommand(menuConfig, stream, cmd, context);
									monitor.worked(1);
//									Thread.sleep(1000);
								}
								
								monitor.done();
							}
							
						});
					} catch (Exception e) {
						if(debug)
							CodegenPlugin.getDefault().consoleLog("widgetSelected error: "+e.getMessage());
						CodegenPlugin.log(e);
					}
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					System.out.println("widgetDefaultSelected");
				}
			});
		}
	}
	
	protected void executeMenuCommand(final MenuConfig menuConfig, final MessageConsoleStream stream, String cmd, Map<String, String> context){
		if(expr.isExpresstion(cmd)){
			cmd = expr.parse(cmd, context);
		}
		if(menuConfig.canPrintinfo()){
			CodegenPlugin.getDefault().consoleLog("==================== "+DateUtil.nowString()+" ["+cmd+"] start execute  ===================");
			SysUtils.exec(cmd, null, directory, new AExecute() {
				
				@Override
				public Object execute(Object... args) {
					if(ArrayUtils.hasNotElement(args))
						return null;
					String info = args[0].toString();
					CodegenPlugin.getDefault().consoleLog(stream, info);
					return null;
				}
			});
//			CodegenPlugin.getDefault().consoleLog("["+cmd+"] executed : " + rs);
			CodegenPlugin.getDefault().consoleLog("==================== "+DateUtil.nowString()+" ["+cmd+"] has executed ! ===================\n\n\n\n");
		}else{
			try {
				Runtime.getRuntime().exec(cmd, null, directory);
			} catch (IOException e) {
				if(debug)
					CodegenPlugin.getDefault().consoleLog("executeMenuCommand error: "+e.getMessage());
				CodegenPlugin.log(e);
			}
		}
	}
	
    public boolean isDynamic() {
        return true;
    }
}
