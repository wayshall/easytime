package org.onetwo.eclipse.custommenu.handlers;

import org.onetwo.eclipse.Constant.AntDir;

public class CreateWebBuildFileHandler extends CreateBuildFileHandler {
	
	protected String getBaseBuildFile(){
		return AntDir.web;
	}


}
