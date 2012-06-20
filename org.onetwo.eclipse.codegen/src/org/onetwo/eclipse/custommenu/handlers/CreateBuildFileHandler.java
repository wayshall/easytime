package org.onetwo.eclipse.custommenu.handlers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.onetwo.core.codegenerator.util.TemplateContext;
import org.onetwo.core.util.AExecute;
import org.onetwo.core.util.FileUtils;
import org.onetwo.core.util.StringUtils;
import org.onetwo.eclipse.Constant.AntDir;
import org.onetwo.eclipse.PluginUtils;
import org.onetwo.eclipse.codegen.CodegenPlugin;
import org.onetwo.eclipse.custommenu.AntFileTemplate;
import org.onetwo.eclipse.custommenu.preference.CustemMenuPreferencePage.FieldNames;

public class CreateBuildFileHandler extends AbstractHandler {
	
	public static class BuildTemplateContext implements TemplateContext {
		
		private Map<String, String> context;
		private String outfile;
		private String template;
		
		public BuildTemplateContext(Map<String, String> context, String outfile, String template) {
			super();
			this.context = context;
			this.outfile = outfile;
			this.template = template;
		}

		@Override
		public String getOutfile() {
			return outfile;
		}

		@Override
		public String getTemplate() {
			return template;
		}

		@Override
		public Map getContext() {
			return context;
		}

		@Override
		public Object get(String key) {
			return context.get(key);
		}
		
	}
	
	protected Map<String, String> createContext(Object selected, File directory){
		Map<String, String> context = new HashMap<String, String>();
		context.put("cur_dir", directory.toString());
		context.put("dir", directory.toString());
		context.put("selected", selected.toString());
		if(selected instanceof IProject){
			IProject iprj = (IProject)selected;
			context.put("project_name", iprj.getName());
		}
		return context;
	}
	
	protected TemplateContext createTemplateContext(Map<String, String> context, File directory){
		return new BuildTemplateContext(context, directory.getPath() + "/build.xml", AntDir.build_ftl);
	}
	
	protected String getBaseBuildFile(){
		return AntDir.ejb;
	}
	
	protected void copyBaseBuildFileTo(File dir){
		String bfile = getBaseBuildFile();
		File destFile = new File(dir, "build-base.xml");
		FileUtils.copyFile(new File(bfile), destFile);
	}

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final Object selected = PluginUtils.getCurrentSelected(event);
		if(selected==null)
			return null;
		
		if(!(selected instanceof IProject)){
			PluginUtils.showMessage("提示", "请选取项目进行此操作！");
			return null;
		}
		
		final File directory = PluginUtils.getDirectory(selected);
		copyBaseBuildFileTo(directory);
		final String webserverDir = CodegenPlugin.getDefault().getPreferenceString(FieldNames.WEBSERVER_DEPLOY_DIR, null);
		if(StringUtils.isBlank(webserverDir) || !new File(webserverDir).exists()){
			PluginUtils.showMessage("提示", "请先正确设置发布目录");
			return null;
		}
		
		PluginUtils.runInProgress("请稍候...", new AExecute() {
			
			@Override
			public Object execute(Object... args) {
				final Map<String, String> context = createContext(selected, directory);
				context.put("deploy_dir", webserverDir);
				
				TemplateContext tcontext = createTemplateContext(context, directory);
				AntFileTemplate.getInstance().generateFile(tcontext);
				CodegenPlugin.getDefault().consoleLog("构建文件已生成！");
				return null;
			}
		});
		
		return null; 
	}

}
