package org.onetwo.core.codegenerator.util;

import java.util.Map;

import org.onetwo.core.codegenerator.GeneratorConfig;
import org.onetwo.core.codegenerator.db.meta.TableInfo;


@SuppressWarnings("unchecked")
public interface TemplateContextBuilder {
	
	public String getName();
	
	public TemplateContext buildTemplateContext(TableInfo table, GeneratorConfig config, Map context);
 
}
