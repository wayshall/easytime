package org.onetwo.core.codegenerator.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.onetwo.core.beanfactory.InitializeBean;
import org.onetwo.core.codegenerator.generator.Generator;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

@SuppressWarnings("unchecked")
public class FreemarkerTemplate implements InitializeBean, Generator{

	private Configuration cfg;
	
	private String encoding;
	
	private String templateDir;
	
	public FreemarkerTemplate(){
	}

	public void init() {
		try {
			this.cfg = new Configuration();
			this.cfg.setObjectWrapper(new DefaultObjectWrapper());
			this.cfg.setOutputEncoding(this.encoding);
			this.cfg.setDirectoryForTemplateLoading(new File(templateDir));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void generateFile(TemplateContext context){
		if(context==null){
			System.err.println("pre context no table primaryKey. igonre generated file.");
			return ;
		}
		BufferedWriter out = null;
		try {
			
			Template template = cfg.getTemplate(context.getTemplate());
			GenerateUtils.makeDirs(new File(context.getOutfile()).getParent());
			
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(context.getOutfile()), this.encoding));
			context.getContext().put("utils", GenerateUtils.getInstance());
			template.process(context, out);
			System.out.println("file is generate : " + context.getOutfile());
			
		}catch(Exception e){
			throw new RuntimeException("generate file error : " + context.getOutfile(), e);
		} finally{
			if(out!=null)
				try {
					out.close();
				} catch (IOException e) {
					throw new RuntimeException("close io error : " + context.getOutfile(), e);
				}
		}
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setTemplateDir(String templateDir) {
		this.templateDir = templateDir;
	}

}
