package org.onetwo.eclipse.custommenu;

import org.onetwo.core.codegenerator.util.FreemarkerTemplate;
import org.onetwo.core.codegenerator.util.TemplateContext;
import org.onetwo.eclipse.PluginUtils;

public class AntFileTemplate {

	private static final AntFileTemplate instance = new AntFileTemplate();
	
	
	public static AntFileTemplate getInstance() {
		return instance;
	}

	private FreemarkerTemplate tempalte;
	
	private AntFileTemplate(){
		this.tempalte = new FreemarkerTemplate();
		this.tempalte.setEncoding("UTF-8");
		this.tempalte.setTemplateDir(PluginUtils.getPluginPath()+"/ant");
		this.tempalte.init();
	}

	public void generateFile(TemplateContext context) {
		tempalte.generateFile(context);
	}
	
}
